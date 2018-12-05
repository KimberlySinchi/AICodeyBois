package org.firstinspires.ftc.teamcode.Detectors;

import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.DogeCVDetector;
import com.disnodeteam.dogecv.filters.DogeCVColorFilter;
import com.disnodeteam.dogecv.filters.HSVRangeFilter;
import com.disnodeteam.dogecv.filters.LeviColorFilter;
import com.disnodeteam.dogecv.scoring.MaxAreaScorer;
import com.disnodeteam.dogecv.scoring.PerfectAreaScorer;
import com.disnodeteam.dogecv.scoring.RatioScorer;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class SampleAlignDetector extends DogeCVDetector
{
    public enum GoldLocation
    {
        UNKNOWN,
        LEFT,
        CENTER,
        RIGHT
    }

    private Mat workingMat  = new Mat();
    private Mat displayMat  = new Mat();
    private Mat yellowMask  = new Mat();
    private Mat whiteMask   = new Mat();
    private Mat hierarchy     = new Mat();

    private boolean found    = false;
    private boolean aligned  = false;
    private double  goldXPos = 0;

    public boolean debugAlignment = true;
    public double alignPosOffset  = 0;
    public double alignSize       = 100;

    public DogeCV.AreaScoringMethod areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA;

    public RatioScorer ratioScorer             = new RatioScorer(1.0,5);
    public MaxAreaScorer maxAreaScorer         = new MaxAreaScorer(0.01);
    public PerfectAreaScorer perfectAreaScorer = new PerfectAreaScorer(5000,0.05);

    public DogeCVColorFilter yellowFilter = new LeviColorFilter(LeviColorFilter.ColorPreset.YELLOW,100);
    public DogeCVColorFilter whiteFilter  = new HSVRangeFilter(new Scalar(0,0,200), new Scalar(50,40,255));

    private SamplingOrderDetector.GoldLocation currentOrder = SamplingOrderDetector.GoldLocation.UNKNOWN;
    private SamplingOrderDetector.GoldLocation lastOrder    = SamplingOrderDetector.GoldLocation.UNKNOWN;
    private boolean      isFound      = false;

    public SampleAlignDetector()
    {
        super();
        this.detectorName = "Sampling Order Align Detector";
    }

    @Override
    public Mat process(Mat input)
    {
        input.copyTo(displayMat);
        input.copyTo(workingMat);
        input.release();

        //Imgproc.GaussianBlur(workingMat,workingMat,new Size(5,5),0);
        yellowFilter.process(workingMat.clone(), yellowMask);
        whiteFilter.process(workingMat.clone(), whiteMask);

        List<MatOfPoint> contoursYellow = new ArrayList<>();
        List<MatOfPoint> contoursWhite = new ArrayList<>();

        Imgproc.blur(whiteMask,whiteMask,new Size(2,2));
        Imgproc.blur(yellowMask,yellowMask,new Size(2,2));

        Imgproc.findContours(yellowMask, contoursYellow, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(displayMat,contoursYellow,-1,new Scalar(230,70,70),2);

        Imgproc.findContours(whiteMask, contoursWhite, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(displayMat,contoursWhite,-1,new Scalar(230,70,70),2);

        Rect chosenYellowRect  = null;
        double chosenYellowScore = Integer.MAX_VALUE;

        MatOfPoint2f approxCurve = new MatOfPoint2f();

        for(MatOfPoint c : contoursYellow)
        {
            MatOfPoint2f contour2f = new MatOfPoint2f(c.toArray());

            double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
            Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);

            //Convert back to MatOfPoint
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());

            // Get bounding rect of contour
            Rect rect = Imgproc.boundingRect(points);

            double diffrenceScore = calculateScore(points);

            if(diffrenceScore < chosenYellowScore && diffrenceScore < maxDifference)
            {
                chosenYellowScore = diffrenceScore;
                chosenYellowRect = rect;
            }

            double area = Imgproc.contourArea(c);
            double x = rect.x;
            double y = rect.y;
            double w = rect.width;
            double h = rect.height;
            Point centerPoint = new Point(x + ( w/2), y + (h/2));
            if( area > 500)
            {
                Imgproc.circle(displayMat,centerPoint,3,new Scalar(0,255,255),3);
                Imgproc.putText(displayMat,"Area: " + area,centerPoint,0,0.5,new Scalar(0,255,255));
            }
        }

        double alignX    = (getAdjustedSize().width / 2) + alignPosOffset;
        double alignXMin = alignX - (alignSize / 2);
        double alignXMax = alignX +(alignSize / 2);
        double xPos;

        if(chosenYellowRect != null)
        {
            Imgproc.rectangle(displayMat, chosenYellowRect.tl(), chosenYellowRect.br(), new Scalar(255,0,0),4);
            Imgproc.putText(displayMat, "Chosen", chosenYellowRect.tl(),0,1,new Scalar(255,255,255));

            xPos = chosenYellowRect.x + (chosenYellowRect.width / 2);
            goldXPos = xPos;

            Imgproc.circle(displayMat, new Point( xPos, chosenYellowRect.y + (chosenYellowRect.height / 2)), 5, new Scalar(0,255,0),2);

            if(xPos < alignXMax && xPos > alignXMin)
                aligned = true;
            else
                aligned = false;

            Imgproc.putText(displayMat,"Current X: " + chosenYellowRect.x,new Point(10,getAdjustedSize().height - 30),0,0.5, new Scalar(255,255,255),1);
            found = true;
        }
        else
        {
            found = false;
            aligned = false;
        }
        if(debugAlignment)
        {
            if(isFound())
                Imgproc.line(displayMat,new Point(goldXPos, getAdjustedSize().height), new Point(goldXPos, getAdjustedSize().height - 30),new Scalar(255,255,0), 2);

            Imgproc.line(displayMat,new Point(alignXMin, getAdjustedSize().height), new Point(alignXMin, getAdjustedSize().height - 40),new Scalar(0,255,0), 2);
            Imgproc.line(displayMat,new Point(alignXMax, getAdjustedSize().height), new Point(alignXMax,getAdjustedSize().height - 40),new Scalar(0,255,0), 2);
        }
        Imgproc.putText(displayMat,"Result: " + aligned,new Point(10,getAdjustedSize().height - 50),0,1, new Scalar(255,255,0),1);

        List<Rect>   choosenWhiteRect  = new ArrayList<>(2);
        List<Double> chosenWhiteScore  = new ArrayList<>(2);
        chosenWhiteScore.add(0, Double.MAX_VALUE);
        chosenWhiteScore.add(1, Double.MAX_VALUE);
        choosenWhiteRect.add(0, null);
        choosenWhiteRect.add(1, null);

        for(MatOfPoint c : contoursWhite)
        {
            MatOfPoint2f contour2f = new MatOfPoint2f(c.toArray());

            double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
            Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);

            MatOfPoint points = new MatOfPoint(approxCurve.toArray());

            Rect rect = Imgproc.boundingRect(points);

            double differenceScore = calculateScore(points);

            double area = Imgproc.contourArea(c);
            double x = rect.x;
            double y = rect.y;
            double w = rect.width;
            double h = rect.height;
            Point centerPoint = new Point(x + ( w/2), y + (h/2));
            if( area > 1000)
            {
                Imgproc.circle(displayMat,centerPoint,3,new Scalar(0,255,255),3);
                Imgproc.putText(displayMat,"Area: " + area,centerPoint,0,0.5,new Scalar(0,255,255));
                Imgproc.putText(displayMat,"Diff: " + differenceScore,new Point(centerPoint.x, centerPoint.y + 20),0,0.5,new Scalar(0,255,255));
            }

            boolean good = true;
            if(differenceScore < maxDifference && area > 1000)
            {
                if(differenceScore < chosenWhiteScore.get(0))
                {
                    choosenWhiteRect.set(0,rect);
                    chosenWhiteScore.set(0,differenceScore);
                }
                else if(differenceScore < chosenWhiteScore.get(1) && differenceScore > chosenWhiteScore.get(0))
                {
                    choosenWhiteRect.set(1,rect);
                    chosenWhiteScore.set(1, differenceScore);
                }
            }
        }

        //Draw found gold element
        if(chosenYellowRect != null)
        {
            Imgproc.rectangle(displayMat,
                    new Point(chosenYellowRect.x, chosenYellowRect.y),
                    new Point(chosenYellowRect.x + chosenYellowRect.width, chosenYellowRect.y + chosenYellowRect.height),
                    new Scalar(255, 0, 0), 2);

            Imgproc.putText(displayMat,
                    "Gold: " + String.format("%.2f X=%.2f", chosenYellowScore, (double)chosenYellowRect.x),
                    new Point(chosenYellowRect.x - 5, chosenYellowRect.y - 10),
                    Core.FONT_HERSHEY_PLAIN,
                    1.3,
                    new Scalar(0, 255, 255),
                    2);

        }
        for(int i=0;i<choosenWhiteRect.size();i++)
        {
            Rect rect = choosenWhiteRect.get(i);
            if(rect != null)
            {
                double score = chosenWhiteScore.get(i);
                Imgproc.rectangle(displayMat,
                        new Point(rect.x, rect.y),
                        new Point(rect.x + rect.width, rect.y + rect.height),
                        new Scalar(255, 255, 255), 2);
                Imgproc.putText(displayMat,
                        "Silver: " + String.format("Score %.2f ", score) ,
                        new Point(rect.x - 5, rect.y - 10),
                        Core.FONT_HERSHEY_PLAIN,
                        1.3,
                        new Scalar(255, 255, 255),
                        2);
            }
        }

        if(choosenWhiteRect.get(0) != null && choosenWhiteRect.get(1) != null  && chosenYellowRect != null)
        {
            int leftCount = 0;
            for(int i=0;i<choosenWhiteRect.size();i++)
            {
                Rect rect = choosenWhiteRect.get(i);
                if(chosenYellowRect.x > rect.x)
                    leftCount++;
            }
            if(leftCount == 0)
                currentOrder = SamplingOrderDetector.GoldLocation.LEFT;
            if(leftCount == 1)
                currentOrder = SamplingOrderDetector.GoldLocation.CENTER;
            if(leftCount >= 2)
                currentOrder = SamplingOrderDetector.GoldLocation.RIGHT;
            isFound = true;
            lastOrder = currentOrder;
        }
        else
        {
            currentOrder = SamplingOrderDetector.GoldLocation.UNKNOWN;
            isFound = false;
        }

        //Display Debug Information
        Imgproc.putText(displayMat,"Gold Position: " + lastOrder.toString(),new Point(10,getAdjustedSize().height - 80),0,1, new Scalar(255,255,0),1);
        Imgproc.putText(displayMat,"Current Track: " + currentOrder.toString(),new Point(10,getAdjustedSize().height - 10),0,0.5, new Scalar(255,255,255),1);

        return displayMat;
    }

    @Override
    public void useDefaults()
    {
        if(areaScoringMethod == DogeCV.AreaScoringMethod.MAX_AREA)
            addScorer(maxAreaScorer);
        if (areaScoringMethod == DogeCV.AreaScoringMethod.PERFECT_AREA)
            addScorer(perfectAreaScorer);
        addScorer(ratioScorer);
    }
    public boolean isFound()
    {
        return isFound;
    }
    public SamplingOrderDetector.GoldLocation getCurrentOrder()
    {
        return currentOrder;
    }
    public SamplingOrderDetector.GoldLocation getLastOrder()
    {
        return lastOrder;
    }
    public void setAlignSettings(int offset, int width)
    {
        alignPosOffset = offset;
        alignSize = width;
    }
    public boolean getAligned()
    {
        return aligned;
    }
    public double getXPosition()
    {
        return goldXPos;
    }
}


