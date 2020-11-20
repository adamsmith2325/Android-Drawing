package edu.luc.etl.cs313.android.shapes.model;

/**
 * A visitor to compute the number of basic shapes in a (possibly complex)
 * shape.
 */
public class Count implements Visitor<Integer> {

	protected Integer count = 0;

	@Override
	public Integer onPolygon(final Polygon p) {
		return ++count;
	}

	@Override
	public Integer onCircle(final Circle c) {
		return ++count;
	}

	@Override
	public Integer onGroup(final Group g) {
		countUpShapes(g);
		return count;
	}

	@Override
	public Integer onRectangle(final Rectangle q) {
		return ++count;
	}

	@Override
	public Integer onOutline(final Outline o) {
		if (o.getShape() instanceof Group) {
			Group g = (Group) o.getShape();
			countUpShapes(g);
		} else if (o.getShape() instanceof StrokeColor) {
			StrokeColor sc = (StrokeColor) o.getShape();
			onStrokeColor(sc);
		} else if (o.getShape() instanceof Location) {
			Location location2 = (Location) o.getShape();
			onLocation(location2);
		} else if (o.getShape() instanceof Fill) {
			Fill f = (Fill) o.getShape();
			onFill(f);
		} else {
			count++;
		}
		return count;
	}

	@Override
	public Integer onFill(final Fill c) {
		if (c.getShape() instanceof Group) {
			Group g = (Group) c.getShape();
			countUpShapes(g);
		} else if (c.getShape() instanceof StrokeColor) {
			StrokeColor sc = (StrokeColor) c.getShape();
			onStrokeColor(sc);
		} else if (c.getShape() instanceof Location) {
			Location location2 = (Location) c.getShape();
			onLocation(location2);
		} else if (c.getShape() instanceof Fill) {
			Fill f = (Fill) c.getShape();
			onFill(f);
		} else {
			count++;
		}
		return count;
	}

	@Override
	public Integer onLocation(final Location l) {
		System.out.println(l.getShape() instanceof Group);
		System.out.println(l.getShape());
		if (l.getShape() instanceof Group) {
			Group g = (Group) l.getShape();
			countUpShapes(g);
		} else if (l.getShape() instanceof StrokeColor) {
			StrokeColor sc = (StrokeColor) l.getShape();
			onStrokeColor(sc);
		}  else if (l.getShape() instanceof Location) {
			Location location2 = (Location) l.getShape();
			onLocation(location2);
		} else if (l.getShape() instanceof Fill) {
			Fill f = (Fill) l.getShape();
			onFill(f);
		} else {
			count++;
		}
		return count;
	}

	@Override
	public Integer onStrokeColor(final StrokeColor c) {
		if (c.getShape() instanceof Group) {
			Group g = (Group) c.getShape();
			countUpShapes(g);
		} else if (c.getShape() instanceof StrokeColor) {
			StrokeColor sc = (StrokeColor) c.getShape();
			onStrokeColor(sc);
		} else if (c.getShape() instanceof Fill) {
			Fill f = (Fill) c.getShape();
			onFill(f);
		} else {
			count++;
		}
		return count;
	}

	private void countUpShapes(Group group) {
		for (Shape shape : group.getShapes()) {
			if (shape instanceof Group) {
				Group g = (Group) shape;
				countUpShapes(g);
			} else if (shape instanceof Location) {
				Location l = (Location) shape;
				onLocation(l);
			} else {
				++count;
			}
		}
	}
}
