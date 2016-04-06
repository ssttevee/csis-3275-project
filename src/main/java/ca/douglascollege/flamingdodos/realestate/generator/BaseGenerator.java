package ca.douglascollege.flamingdodos.realestate.generator;

import java.util.ArrayList;
import java.util.List;

public class BaseGenerator {
    private int mWidth;
    private List<Line> lines = new ArrayList<>();

    public BaseGenerator() {
        mWidth = 0;
    }

    public BaseGenerator(int width) {
        mWidth = width;
    }

    public Line addLine() {
        Line line = new Line();
        lines.add(line);
        return line;
    }

    public void addBlankLine() {
        lines.add(new BlankLine());
    }

    public void addSeparator() {
        lines.add(new Separator());
    }

    public String toString() {
        String result = "";

        for (Line line : lines) {
            result += line.toString() + '\n';
        }

        return result;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public class Line {
        private float mTotalWeight = 0;
        private List<Segment> mSegments = new ArrayList<>();

        public Line addSegment(float weight, Object value) {
            mTotalWeight += Math.abs(weight);
            mSegments.add(new Segment(weight, value));

            return this;
        }

        public String toString() {
            String result = "";

            for (Segment segment : mSegments) {
                result += String.format("%1$" + (int) (segment.mWeight / mTotalWeight * mWidth) + "s", segment.mValue);
            }

            return result;
        }

        private class Segment {
            private float mWeight;
            private Object mValue;

            private Segment(float weight, Object value) {
                mWeight = weight;
                mValue = value;
            }
        }
    }

    private class BlankLine extends Line {
        public String toString() {
            return "";
        }
    }

    private class Separator extends Line {
        public String toString() {
            return String.format("%" + mWidth + "s", "").replace(' ', '\u2014');
        }
    }
}
