/*
 * Copyright 2010, 2011, 2012, 2013 mapsforge.org
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.mapsforge.map.layer.renderer;

import java.util.List;

import org.mapsforge.core.model.Point;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

class CanvasRasterer {
	private final Canvas canvas;
	private final Path path;
	private final Matrix symbolMatrix;
	private final Paint bitmapPaint;
	CanvasRasterer() {
		this.canvas = new Canvas();
		this.symbolMatrix = new Matrix();
		this.path = new Path();
		this.bitmapPaint = new Paint();
		this.bitmapPaint.setAntiAlias(true);
		this.bitmapPaint.setFilterBitmap(true);
	}

	void drawNodes(List<PointTextContainer> pointTextContainers) {
		for (int index = pointTextContainers.size() - 1; index >= 0; --index) {
			PointTextContainer pointTextContainer = pointTextContainers.get(index);

			if (pointTextContainer.paintBack != null) {
				this.canvas.drawText(pointTextContainer.text, (int) pointTextContainer.x, (int) pointTextContainer.y,
						pointTextContainer.paintBack);
			}

			this.canvas.drawText(pointTextContainer.text, (int) pointTextContainer.x, (int) pointTextContainer.y,
					pointTextContainer.paintFront);
		}
	}

	void drawSymbols(List<SymbolContainer> symbolContainers) {
		for (int index = symbolContainers.size() - 1; index >= 0; --index) {
			SymbolContainer symbolContainer = symbolContainers.get(index);

			Point point = symbolContainer.point;
			this.symbolMatrix.reset();

			if (symbolContainer.alignCenter) {
				int pivotX = symbolContainer.symbol.getWidth() / 2;
				int pivotY = symbolContainer.symbol.getHeight() / 2;
				this.symbolMatrix.preTranslate((float) (point.x - pivotX), (float) (point.y - pivotY));
				this.symbolMatrix.preRotate((float)Math.toDegrees(symbolContainer.theta), pivotX, pivotY);
			} else {
				this.symbolMatrix.preTranslate((float) point.x, (float) point.y);
				this.symbolMatrix.preRotate((float)Math.toDegrees(symbolContainer.theta));
			}


			this.canvas.drawBitmap(symbolContainer.symbol, this.symbolMatrix,bitmapPaint);
		}
	}

	void drawWayNames(List<WayTextContainer> wayTextContainers) {
		for (int index = wayTextContainers.size() - 1; index >= 0; --index) {
			WayTextContainer wayTextContainer = wayTextContainers.get(index);
			
			Path path = new Path();
			path.moveTo(wayTextContainer.x1, wayTextContainer.y1);
			path.lineTo(wayTextContainer.x2, wayTextContainer.y2);
			this.canvas.drawTextOnPath(wayTextContainer.text, path, 0, 3,wayTextContainer.paint);
		}
	}

	void drawWays(List<List<List<ShapePaintContainer>>> drawWays) {
		int levelsPerLayer = drawWays.get(0).size();

		for (int layer = 0, layers = drawWays.size(); layer < layers; ++layer) {
			List<List<ShapePaintContainer>> shapePaintContainers = drawWays.get(layer);

			for (int level = 0; level < levelsPerLayer; ++level) {
				List<ShapePaintContainer> wayList = shapePaintContainers.get(level);

				for (int index = wayList.size() - 1; index >= 0; --index) {
					drawShapePaintContainer(wayList.get(index));
				}
			}
		}
	}

	void fill(int color) {
		this.canvas.drawColor(color);
	}

	void setCanvasBitmap(Bitmap bitmap) {
		this.canvas.setBitmap(bitmap);
	}

	private void drawCircleContainer(ShapePaintContainer shapePaintContainer) {
		CircleContainer circleContainer = (CircleContainer) shapePaintContainer.shapeContainer;
		Point point = circleContainer.point;
		this.canvas.drawCircle((int) point.x, (int) point.y, (int) circleContainer.radius, shapePaintContainer.paint);
	}

	private void drawPath(ShapePaintContainer shapePaintContainer, Point[][] coordinates) {
		this.path.rewind();

		for (int j = 0; j < coordinates.length; ++j) {
			Point[] points = coordinates[j];
			if (points.length >= 2) {
				Point point = points[0];
				this.path.moveTo((int) point.x, (int) point.y);
				for (int i = 1; i < points.length; ++i) {
					point = points[i];
					this.path.lineTo((int) point.x, (int) point.y);
				}
			}
		}

		this.canvas.drawPath(this.path, shapePaintContainer.paint);
	}

	private void drawShapePaintContainer(ShapePaintContainer shapePaintContainer) {
		ShapeType shapeType = shapePaintContainer.shapeContainer.getShapeType();
		switch (shapeType) {
			case CIRCLE:
				drawCircleContainer(shapePaintContainer);
				return;

			case POLYLINE:
				PolylineContainer polylineContainer = (PolylineContainer) shapePaintContainer.shapeContainer;
				drawPath(shapePaintContainer, polylineContainer.coordinates);
				return;
		}
	}
}
