package com.labeneko.androidgames.framework.math;

public class CollisionChecker {
	//Method
	public static boolean collisionCircles(Circle c1, Circle c2) {
		float distance = c1.center.distSquared(c2.center);
		float radiusSum = c1.radius + c2.radius;
		return distance <= radiusSum * radiusSum;
	}
	
	public static boolean collisionRectangles(Rectangle r1, Rectangle r2) {
		if (r1.lowerLeft.wX < (r2.lowerLeft.wX + r2.width) && (r1.lowerLeft.wX + r1.width) > r2.lowerLeft.wX &&
			r1.lowerLeft.wY < (r2.lowerLeft.wY + r2.height) && (r1.lowerLeft.wY + r1.height) > r2.lowerLeft.wY) {
			return true;
		} else {
			return false;
		}

	}
	
	public static boolean collisionCircleRectangle(Circle c, Rectangle r) {
		float closestX = c.center.wX;
		float closestY = c.center.wY;
		
		if (c.center.wX < r.lowerLeft.wX) {
			closestX = r.lowerLeft.wX;
		} else if (c.center.wX > r.lowerLeft.wX + r.width) {
			closestX = r.lowerLeft.wX + r.width;
		}
		
		if (c.center.wY < r.lowerLeft.wY) {
			closestY = r.lowerLeft.wY;
		} else if (c.center.wY > r.lowerLeft.wY + r.height) {
			closestY = r.lowerLeft.wY + r.height;
		}
		
		return c.center.distSquared(closestX, closestY) < c.radius * c.radius;
	}
	
	public static boolean pointInCircle(Circle c, Vector2d p) {
		return c.center.distSquared(p) < c.radius * c.radius;
	}
	
	public static boolean pointInCircle(Circle c, float x, float y) {
		return c.center.distSquared(x, y) < c.radius * c.radius;
	}
	
	public static boolean pointInRectangle(Rectangle r, Vector2d p) {
		return r.lowerLeft.wX <= p.wX && r.lowerLeft.wX + r.width >= p.wX &&
				r.lowerLeft.wY <= p.wY && r.lowerLeft.wY + r.height >= p.wY;
	}
	
	public static boolean pointInRectangle(Rectangle r, float x, float y) {
		return r.lowerLeft.wX <= x && r.lowerLeft.wX + r.width >= x &&
				r.lowerLeft.wY <= y && r.lowerLeft.wY + r.height >= y;
	}

}
