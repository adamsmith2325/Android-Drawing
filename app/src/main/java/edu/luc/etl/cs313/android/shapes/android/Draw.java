package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

	// TODO entirely your job (except onCircle)

	private final Canvas canvas;

	private final Paint paint;

	public Draw(final Canvas canvas, final Paint paint) {
		this.canvas = canvas;
		this.paint = paint;
		paint.setStyle(Style.STROKE);
	}

	@Override
	public Void onCircle(final Circle c) {
		canvas.drawCircle(0, 0, c.getRadius(), paint);
		return null;
	}

	@Override
	public Void onStrokeColor(final StrokeColor c) {
		paint.setColor(c.getColor());
		return null;
	}

	@Override
	public Void onFill(final Fill f) {
		paint.setStyle(Style.FILL_AND_STROKE);
		return null;
	}

	@Override
	public Void onGroup(final Group g) {
		for (Shape shape : g.getShapes()) {
			Location location;
			if (shape instanceof Location) {
				location = (Location) shape;
			} else {
				location = new Location(0, 0, shape);
			}
			onLocation(location);
		}
		return null;
	}

	@Override
	public Void onLocation(final Location l) {
		Shape shape = l.getShape();
		canvas.translate(l.getX(), l.getY());
		if (shape instanceof Circle) {
			Circle circle = (Circle) shape;
			canvas.drawCircle(0, 0, circle.getRadius(), paint);
		} else if (shape instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) shape;
			canvas.drawRect(0, 0, rectangle.getWidth(), rectangle.getHeight(), paint);
		} else if (shape instanceof Polygon) {
			Polygon polygon = (Polygon) shape;
			float [] points = new float[12];
			int count = 0;
			for (Point point : polygon.getPoints()) {
				if (count == points.length) {
					points = Arrays.copyOf(points, points.length * 2);
				}
				points[count] = point.getX();
				points[count] = point.getY();
				count++;
			}
			canvas.drawLines(points, paint);
		} else if (shape instanceof Outline) {
			Outline outline = (Outline) shape;
			paint.setStyle(Style.STROKE);
			Location location = new Location(l.getX(), l.getY(), outline.getShape());
			onLocation(location);
			paint.setStyle(Style.FILL);
		} else if (shape instanceof Fill) {
			Fill fill = (Fill) shape;
			paint.setStyle(Style.FILL_AND_STROKE);
			Location location = new Location(l.getX(), l.getY(), fill.getShape());
			onLocation(location);
			paint.setStyle(Style.FILL);
		} else if (shape instanceof StrokeColor) {
			StrokeColor strokeColor = (StrokeColor) shape;
			paint.setColor(strokeColor.getColor());
			Location location = new Location(l.getX(), l.getY(), strokeColor.getShape());
			onLocation(location);
			paint.setColor(strokeColor.getColor());
		} else if (shape instanceof Group) {
			Group group = (Group) shape;
			onGroup(group);
		}
		canvas.translate(-l.getX(), -l.getY());
		return null;
	}

	@Override
	public Void onRectangle(final Rectangle r) {
		canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);
		return null;
	}

	@Override
	public Void onOutline(Outline o) {
		paint.setStyle(Style.STROKE);
		return null;
	}

	@Override
	public Void onPolygon(final Polygon s) {

		final float[] pts = null;

		canvas.drawLines(pts, paint);
		return null;
	}
}
