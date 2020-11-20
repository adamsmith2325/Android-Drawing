package edu.luc.etl.cs313.android.shapes.model;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

	private Integer x;
	private Integer y;
	private Integer x2;
	private Integer y2;
	private int minX = 1000;
	private int minY = 1000;
	private int maxX = 0;
	private int maxY = 0;

	@Override
	public Location onCircle(final Circle c) {
		final int radius = c.getRadius();
		return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
	}

	@Override
	public Location onFill(final Fill f) {
		Shape shape = f.getShape();
		if (shape instanceof Outline) {
			Outline outline = (Outline) shape;
			return onOutline(outline);
		} else if (shape instanceof StrokeColor) {
			StrokeColor sc = (StrokeColor) shape;
			return onStrokeColor(sc);
		} else if (shape instanceof Fill) {
			Fill fill = (Fill) shape;
			return onFill(fill);
		} else if (shape instanceof Location) {
			Location location = (Location) shape;
			return onLocation(location);
		} else if (shape instanceof Group) {
			Group group = (Group) shape;
			return onGroup(group);
		}
		return new Location(0, 0, f.getShape());
	}

	@Override
	public Location onGroup(final Group g) {
		setMinAndMaxXAndY(g);
		return new Location(minX, minY, new Rectangle(maxX - minX, maxY - minY));
	}

	@Override
	public Location onLocation(final Location l) {
		Shape shape = l.getShape();
		if (shape instanceof Outline) {
			Outline outline = (Outline) shape;
			return onOutline(outline);
		} else if (shape instanceof StrokeColor) {
			StrokeColor sc = (StrokeColor) shape;
			return onStrokeColor(sc);
		} else if (shape instanceof Fill) {
			Fill fill = (Fill) shape;
			return onFill(fill);
		} else if (shape instanceof Location) {
			Location location = (Location) shape;
			return onLocation(location);
		} else if (shape instanceof Group) {
			Group group = (Group) shape;
			return onGroup(group);
		} else if (shape instanceof Rectangle) {
			return new Location(l.getX(), l.getY(), shape);
		} else if (shape instanceof Circle) {
			Circle c = (Circle) shape;
			int diameter = c.getRadius() * 2;
			return new Location(l.getX(), l.getY(), new Rectangle(diameter, diameter));
		}
		return new Location(l.getX(), l.getY(), l.getShape());
	}

	@Override
	public Location onRectangle(final Rectangle r) {
		return new Location(0, 0, r);
	}

	@Override
	public Location onStrokeColor(final StrokeColor c) {
		Shape shape = c.getShape();
		System.out.println("Stroke Color and " + shape);
		if (shape instanceof Outline) {
			Outline outline = (Outline) shape;
			return onOutline(outline);
		} else if (shape instanceof StrokeColor) {
			StrokeColor sc = (StrokeColor) shape;
			return onStrokeColor(sc);
		} else if (shape instanceof Fill) {
			Fill fill = (Fill) shape;
			return onFill(fill);
		} else if (shape instanceof Location) {
			Location location = (Location) shape;
			return onLocation(location);
		} else if (shape instanceof Group) {
			Group group = (Group) shape;
			return onGroup(group);
		}
		return new Location(0, 0, c.getShape());
	}

	@Override
	public Location onOutline(final Outline o) {
		Shape shape = o.getShape();
		if (shape instanceof Outline) {
			Outline outline = (Outline) shape;
			return onOutline(outline);
		} else if (shape instanceof StrokeColor) {
			StrokeColor sc = (StrokeColor) shape;
			return onStrokeColor(sc);
		} else if (shape instanceof Fill) {
			Fill fill = (Fill) shape;
			return onFill(fill);
		} else if (shape instanceof Location) {
			Location location = (Location) shape;
			return onLocation(location);
		} else if (shape instanceof Group) {
			Group group = (Group) shape;
			return onGroup(group);
		}
		return new Location(0, 0, o.getShape());
	}

	@Override
	public Location onPolygon(final Polygon s) {
		for (Point point : s.getPoints()) {
			int x = point.getX();
			int y = point.getY();
			if (x < minX)
				minX = x;
			if (y < minY)
				minY = y;
			if (x > maxX)
				maxX = x;
			if (y > maxY)
				maxY = y;
		}
		return new Location(minX, minY, new Rectangle(maxX - minX, maxY - minY));
	}

	private void setMinAndMaxXAndY(Group g) {
		x = null;
		y = null;
		x2 = null;
		y2 = null;
		for (Shape shape : g.getShapes()) {
			helper(shape);
			if (x != null && x < minX)
				minX = x;
			if (y != null && y < minY)
				minY = y;
			if (x != null && x > maxX)
				maxX = x;
			if (y != null && y > maxY)
				maxY = y;
			if (x2 != null && x2 > maxX)
				maxX = x2;
			if (y2 != null && y2 > maxY)
				maxY = y2;
		}
	}
	private void helper(Shape shape) {
		if (shape instanceof Rectangle) {
			Rectangle r = (Rectangle) shape;
			x = r.getWidth();
			y = r.getHeight();
		} else if (shape instanceof Circle) {
			Circle c = (Circle) shape;
			x = x2 = c.getRadius() * -1;
			y = y2 = c.getRadius() * -1;
		} else if (shape instanceof Location) {
			Location location = (Location) shape;
			Shape s = location.getShape();
			x = x2 = location.getX();
			y = y2 = location.getY();
			if (s instanceof Circle) {
				Circle c = (Circle) s;
				x -= c.getRadius();
				x2 += c.getRadius();
				y -= c.getRadius();
				y2 += c.getRadius();
			} else if (s instanceof Rectangle) {
				Rectangle r = (Rectangle) s;
				x += r.getWidth();
				y += r.getHeight();
			} else if (s instanceof Group) {
				Group group = (Group) s;
				setMinAndMaxXAndY(group);
			} else {
				helper(shape);
			}

		} else {
			Group group = (Group) shape;
			setMinAndMaxXAndY(group);
}
	}
}
