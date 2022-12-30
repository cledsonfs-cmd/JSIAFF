/***
 * Neuroph  http://neuroph.sourceforge.net
 * Copyright by Neuroph Project (C) 2008
 *
 * This file is part of Neuroph framework.
 *
 * Neuroph is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Neuroph is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Neuroph. If not, see <http://www.gnu.org/licenses/>.
 */

package br.gov.rn.emater.IA.errorgraph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.SwingWorker;
import org.neuroph.nnet.learning.LMS;

/**
 * @author Ivan Trnavac
 * @author Zoran Sevarac
 *
 * TODO: triple buffering implementation not completed yet...
 *
 */
public class GraphComponent extends javax.swing.JComponent implements Observer {

    private Image imageBuffer;
    private Graphics2D graphicsBuffer;
    private GraphScope graphScope;

    private Vector<Point2D> pointsBuffer;
    private int bufferSize = 100000;
    private Point prevPoint = null;
    

    private GraphScope defaultScope;
    private GraphProperties graphProperties;
    private int width,  height;

    private int maxSize = 1700000;
    private Rectangle rec = null;
    private Point mousePoint = null;
    private int zw;
    private int zh; //zoom rectangle width and height
    private boolean zoomOn = true;
    private int pointSize = 1; //point radius
    private int zoomMode = 0; //0, 1 = fix width, 2 = fix height
    private int bufferCounter = 0;
    ConcurrentLinkedQueue pointsQueueBuffer;

    TrainerSwingWorker worker;
    LMS learningRule;

    /**
     * Constructor
     */
     public GraphComponent() {
        initComponents();
        //create graph scopes
        graphScope = new GraphScope();
        defaultScope = graphScope.clone();
        graphProperties = new GraphProperties();
      
        pointsBuffer = new Vector<Point2D>(100000);
        pointsQueueBuffer = new ConcurrentLinkedQueue<Point2D>();
        setSize();

        addPoint(new Point.Double(0, 0));
       
        worker = new TrainerSwingWorker();
        worker.execute();
    }

    /**
     * Sets zoom mode - horizontal or vertical zoom
     * @param mode
     */
    public void setZoomMode(int mode) {
        zoomMode = mode;
    }

    /**
     * Turn on/off the zoome mode
     * @param zoomOn
     */
    public void setZoomOn(boolean zoomOn) {
        this.zoomOn = zoomOn;
    }

    private void setSize() {
        width = graphScope.calculateWidth() + graphProperties.getWestGap() + graphProperties.getEastGap();
        height = graphScope.calculateHeight() + graphProperties.getNorthGap() + graphProperties.getSouthGap();
        setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        zw = 50;
        zh = zw;
    }

    public void resizeMaxX(GraphScope grScope, double value) {
        grScope.setMaxX(grScope.getMaxX() * value);
        grScope.calculatePixelValue(width - graphProperties.getHGap(), height - graphProperties.getWestGap());
        defaultScope = grScope.clone();
        redrawDrawingBuffer();
    }

    public void resizeMaxY(GraphScope grScope, double value) {
        grScope.setMaxY(graphScope.getMaxY() * value);
        grScope.calculatePixelValue(width - graphProperties.getHGap(), height - graphProperties.getWestGap());
        defaultScope = grScope.clone();
        redrawDrawingBuffer();
    }

    public void zoomX(double value) {
        if (value < 1) {
            if (width * height * (2 - value) > maxSize) {
                return;
            }
        }
        graphScope.zoomX(value);
        if (graphScope.calculateWidth() + graphProperties.getVGap() < defaultScope.calculateWidth()) {
            resizeMaxX(graphScope, value);
            return;
        }
        clearGraphics();
        setSize();
        repaint();
    }

    public void zoomY(double value) {
        if (value < 1) {
            if (width * height * (2 - value) > maxSize) {
                return;
            }
        }
        graphScope.zoomY(value);
        clearGraphics();
        setSize();
        repaint();
    }

    /**
     * Gets the scope settings for the current display
     * @return scope settings for the current display
     */
    public GraphScope getScope() {
        return graphScope;
    }

    /**
     * Sets scope settings for the current display
     * @param graphScope  scope settings
     */
    public void setScope(GraphScope grScope) {
        graphScope = grScope;
        graphScope.calculatePixelValue(graphScope.calculateWidth(), graphScope.calculateHeight());
        clearGraphics();
        setSize();
        repaint();   
    }

    /**
     * Returns various settings for this graph as GraphProperties instance
     */
    public GraphProperties getGraphProperties() {
        return graphProperties;
    }

    /**
     *
     * @param graphProperties
     */
    public void setGraphProperties(GraphProperties graphProperties) {
        this.graphProperties = graphProperties;
        clearGraphics();
        setSize();
        repaint();
    }

    public void addPoint(Point2D p) {
        if (p == null) {
            throw new RuntimeException("Null point in method addPoint()!");
        }

        if (pointsBuffer.size() >= bufferSize) {            
            pointsBuffer.clear();
        }
        
        pointsBuffer.add(p);
    }

    /**
     * Used after loading saved points, to draw the onto graph
     */
    public void addPoints(Vector<Point2D> v) {
        // if loaded vector is null do nothing
        if (v.size() == 0) {
            return;
        }
        // make loaded vector be points buffer
        pointsBuffer = v;

        // find graph scale (max x and max y)
        Point2D pMaxX = v.get(0);
        Point2D pMaxY = v.get(0);
        for (int i = 1; i < v.size(); i++) {
            if (v.get(i).getX() > pMaxX.getX()) {
                pMaxX = v.get(i);
            }
            if (v.get(i).getY() > pMaxY.getY()) {
                pMaxY = v.get(i);
            }
        }

        // set graph scope
        Point pX = graphScope.getPoint(pMaxX, graphProperties.getWestGap(), graphProperties.getSouthGap());
        Point pY = graphScope.getPoint(pMaxY, graphProperties.getWestGap(), graphProperties.getSouthGap());

        //provera da li je van okvira if (p.y > getHeight()-northGap) return;
        boolean ret = false;
        if (pX.x > width - graphProperties.getEastGap()) {
            graphScope.setMaxX(pMaxX.getX());
            resizeMaxX(graphScope, 2);
            ret = true;
        }
        if (pY.y > height - graphProperties.getNorthGap()) {
            graphScope.setMaxY(pMaxY.getY());
            resizeMaxY(graphScope, 2);
            ret = true;
        }
        if (ret) {
            graphScope.calculatePixelValue(width - graphProperties.getHGap(), height - graphProperties.getVGap());
            defaultScope = graphScope.clone();
        }
        
        redrawDrawingBuffer();
        repaint();
    }

    private synchronized void reduceBuffer() {
        if (pointsBuffer.size() == bufferSize) {
            for (int i = 0; i < getBufferSize() * 0.2; i++) {
                pointsBuffer.remove(0);
            }
        }
    }

    // writes image buffer to component
    @Override
    public void paintComponent(Graphics g) {
        drawGraph();
        g.drawImage(imageBuffer, 0, 0, this);
    }

    private void drawGraph() {
        if (graphicsBuffer==null) {
            createGraphicsBuffers();
            redrawDrawingBuffer();
        }

        if (zoomOn) {
            drawZoomSelection();
        }

    }

    /**
     * Redraws entire graph buffer - axis and data
     */
    public void redrawDrawingBuffer() {
//        drawBackground(graphicsBuffer1, scope1); // these are not neececery at all... optimize drawing and switching graphic buffers
//        drawBackground(graphicsBuffer2, scope2);
//        drawNumbers(graphicsBuffer1, scope1);
//        drawNumbers(graphicsBuffer2, scope2);
//        drawAxis(graphicsBuffer1, scope1);
//        drawAxis(graphicsBuffer2, scope2);
        drawBackground(graphicsBuffer, graphScope);
        drawNumbers(graphicsBuffer, graphScope);
        drawAxis(graphicsBuffer, graphScope);
        drawEntireGraph(); // draws entire graph in to back buffer
    }

    /**
     * Creates offscreen drawing buffers
     * @return
     */
    private boolean createGraphicsBuffers() {
        if (graphicsBuffer == null) {
            prevPoint = null;
            imageBuffer = createImage(width, height);
            graphicsBuffer = (Graphics2D) imageBuffer.getGraphics();
            graphicsBuffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            return true;
        }
        return false;
    }

    /**
     * Draws graph panel background color to specified drawing buffers
     */
    private void drawBackground(Graphics2D grBuffer, GraphScope grScope) {
        grBuffer.setColor(graphProperties.getBackground());
        grBuffer.fillRect(0, 0, width, height);
        grBuffer.setColor(graphProperties.getFrameColor());
        grBuffer.drawRect(graphProperties.getWestGap(), graphProperties.getNorthGap(), grScope.calculateWidth(), grScope.calculateHeight());
    }

    /**
     * Draws axis to specified drawing buffers
     * @param grBuffer
     * @param grScope
     */
    private void drawAxis(Graphics2D grBuffer, GraphScope grScope) {
        grBuffer.setColor(graphProperties.getAxisColor());
        int x = grScope.calculateX0() + graphProperties.getWestGap();//+1;
        int y = height - grScope.calculateY0() /*+ 1*/ - graphProperties.getSouthGap();
        if (x < graphProperties.getWestGap()) {
            //Axis X
            grBuffer.drawLine(graphProperties.getWestGap(), y, width - graphProperties.getEastGap(), y);
            return;
        }
        //Axis X
        grBuffer.drawLine(graphProperties.getWestGap(), y, width - graphProperties.getEastGap(), y);
        //Axis Y
        grBuffer.drawLine(x, graphProperties.getNorthGap(), x, height - graphProperties.getSouthGap());
    }

    /**
     * Draws point to offscreen buffer. This method does actual 'drawing'.
     */
    private void drawPoint(Point2D point2d) {
        if (point2d != null) {
            // here we should be drawing in both buffers at the same time, and when it gets to end at the currently displayed buffer
            // they just get switched
            Point p1 = graphScope.getPoint(point2d, graphProperties.getWestGap(), graphProperties.getSouthGap());
//            Point p2 = backScope.getPoint(point2d, graphProperties.getWestGap(), graphProperties.getSouthGap());
            
            //provera da li je van okvira if (p.y > getHeight()-northGap) return;
            boolean ret = false;
            if (p1.x > width - graphProperties.getEastGap()) {
                resizeMaxX(graphScope, 2);
               // switchDisplayBuffer();
                ret = true;
            }
            if (p1.y > height - graphProperties.getNorthGap()) {
                resizeMaxY(graphScope, 2);
                ret = true;
            }
            if (ret) {
                return;
            }

            if (p1 != null) {
                if (graphProperties.isVisiblePoints()) {
                    graphicsBuffer.setColor(graphProperties.getPointColor());
                    graphicsBuffer.drawRect(p1.x - pointSize, height - p1.y - pointSize, pointSize * 2, pointSize * 2);
                    //graphicsBuffer.drawOval(p.x - r, h - p.y - r, r*2, r*2);
                }
                if (!graphProperties.isVisibleLine()) {
                    return;
                }
                if (prevPoint != null) {
                    graphicsBuffer.setColor(graphProperties.getLineColor());
                    //crtanje linije je nepotrebno ako se lepo crta grafik - ne gube se tacke
                    graphicsBuffer.drawLine(prevPoint.x, getHeight() - prevPoint.y, p1.x, getHeight() - p1.y);

                }
                prevPoint = p1; // previous point
            } else {
                prevPoint = null;
            }
        }
    }

    /**
     * Draws entire graph from points buffer onto back bauffer
     */
    private void drawEntireGraph() {
        // if points buffer is empty, jump out of this method
        if (pointsBuffer != null && pointsBuffer.size() > 0) {
            Point prev_point = null;

            for (int i = 0; i < pointsBuffer.size(); i++) {
                Point p1 = graphScope.getPoint(pointsBuffer.get(i), graphProperties.getWestGap(), graphProperties.getSouthGap());

                if (prev_point!=null) {
                    if (prev_point.getX() > p1.getX()) prev_point = null;
                }
                
                if (p1 != null) {
                    if (graphProperties.isVisibleLine() && prev_point != null) {
                        graphicsBuffer.setColor(graphProperties.getLineColor());
                        graphicsBuffer.drawLine(prev_point.x, height - prev_point.y, p1.x, height - p1.y);
                    }
                    if (graphProperties.isVisiblePoints()) {
                        graphicsBuffer.setColor(graphProperties.getPointColor());
                        graphicsBuffer.drawRect(p1.x - pointSize, height - p1.y - pointSize, pointSize * 2, pointSize * 2);
                        
                        //graphicsBuffer.drawOval(p.x - r, h - p.y - r, r*2, r*2);

                    }
                }
                prev_point = p1;
            }

        }
    }


     /**
     * Draws zoom area rectangle
     */
    private void drawZoomSelection() {
        graphicsBuffer.setXORMode(Color.white);
        graphicsBuffer.setColor(Color.black);
        if (mousePoint == null) {
            if (rec != null) {
                graphicsBuffer.drawRect(rec.x, rec.y, rec.width, rec.height);
                rec = null;
            }
            graphicsBuffer.setPaintMode();
            return;
        }
        if (rec != null) {
            graphicsBuffer.drawRect(rec.x, rec.y, rec.width, rec.height);

        }
        switch (zoomMode) {
            case 0:
                rec = new Rectangle(mousePoint.x - zw / 2, mousePoint.y - zh / 2, zw, zh);
                break;
            case 1:
                rec = new Rectangle(graphProperties.getWestGap(), mousePoint.y - zh / 2, width - graphProperties.getHGap(), zh);
                break;
            case 2:
                rec = new Rectangle(mousePoint.x - zw / 2, graphProperties.getNorthGap(), zw, height - graphProperties.getVGap());
                break;
        }
        
        graphicsBuffer.drawRect(rec.x, rec.y, rec.width, rec.height);
        graphicsBuffer.setPaintMode();
    }

    private void clearGraphics() {
        if (graphicsBuffer != null) {
            graphicsBuffer.clearRect(0, 0, width, height);
        }
        graphicsBuffer = null;
    }

    public void clearBuffer() {
        pointsBuffer.clear();
    }

    public void clear() {
        clearGraphics();
        clearBuffer();
        repaint();
    }

    private void drawNumbers(Graphics2D grBuffer, GraphScope grScope) {
        Stroke stroke1 = grBuffer.getStroke();
        float[] dashPattern = {1, 3};
        Stroke stroke2 = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0);
        grBuffer.setStroke(stroke2);
        grBuffer.setFont(new Font("Arial", Font.PLAIN, 10)); //Font f; f.getSize()
        //graphicsBuffer.setColor(foreground);
        double numX = grScope.getMinX();
        while (numX <= grScope.getMaxX()) {
            Point p = grScope.getPoint(new Point2D.Double(numX, grScope.getMinY()), graphProperties.getWestGap(), graphProperties.getSouthGap());
            grBuffer.setColor(graphProperties.getNumColor());
            grBuffer.drawString(graphProperties.getDecimalFormatX().format(numX), p.x - 10, height - p.y + 10);
            /*graphicsBuffer.drawLine(p.x, getHeight() - southGap, p.x, getHeight() - southGap - 5);
            graphicsBuffer.drawLine(p.x, northGap, p.x, northGap + 5);*/
            if (graphProperties.isVisibleGrid()) {
                grBuffer.setColor(graphProperties.getGridColor());
                grBuffer.drawLine(p.x, graphProperties.getNorthGap(), p.x, height - graphProperties.getSouthGap());//vertical
            }
            //numX += view.getNumPeriodX();
            numX = (double) (Math.round(numX + graphProperties.getFixPixNumPeriodX() * grScope.getPixValueX()) + 1);
        }
        double numY = grScope.getMinY();
        while (numY <= grScope.getMaxY()) {
            Point p = grScope.getPoint(new Point2D.Double(0, numY), graphProperties.getWestGap(), graphProperties.getSouthGap());
            //Point p = scope.getPoint(new Point2D.Double(scope.getMinX(), numY), view.getWestGap(), view.getSouthGap());
            grBuffer.setColor(graphProperties.getNumColor());
            grBuffer.drawString(graphProperties.getDecimalFormatY().format(numY), graphProperties.getWestGap() - 25, height - p.y + 3);

            //graphicsBuffer.drawLine(westGap, getHeight() - p.y, westGap + 5, getHeight() - p.y);
            if (graphProperties.isVisibleGrid()) {
                grBuffer.setColor(graphProperties.getGridColor());
                grBuffer.drawLine(graphProperties.getWestGap(), height - p.y, width - graphProperties.getEastGap(), height - p.y);//horizontal
            }
           numY += graphProperties.getNumPeriodY();
            //numY = Double.parseDouble(view.getDecimalFormatY().format(numY + view.getFixPixNumPeriodY() * scope.getPixValueY()));
        }
        grBuffer.setStroke(stroke1);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                formMouseWheelMoved(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                formMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
    }// </editor-fold>//GEN-END:initComponents
    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        if (!zoomOn) {
            return;
        }
        if (rec == null) {
            return;
        }
        if (evt.getButton() == MouseEvent.BUTTON1) {
            defaultScope = graphScope.clone();
            graphScope.crop(new Point(rec.x + rec.width, height - rec.y), new Point(rec.x, height - (rec.y + rec.height)),
                    graphProperties.getWestGap(), graphProperties.getSouthGap());
            graphScope.calculatePixelValue(width - graphProperties.getHGap(), height - graphProperties.getVGap());
            setSize();
            clearGraphics();
            //ili
            //redraw();
            rec = null;
            repaint();
        } else if (evt.getButton() == MouseEvent.BUTTON3) {
            rec = null;
            setScope(defaultScope);
        }       
    }//GEN-LAST:event_formMousePressed

    private void formMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_formMouseWheelMoved
        if (!zoomOn) {
            return;
        }
        double v = 1;
        switch (evt.getWheelRotation()) {
            case -1:
                v = 0.91;
                break;
            case 1:
                v = 1.1;
                break;

        }
        switch (zoomMode) {
            case 0:
                zw *= v;
                zh *= v;
                break;
            case 1:
                zh *= v;
                break;
            case 2:
                zw *= v;
                break;

        }
        repaint();
    }//GEN-LAST:event_formMouseWheelMoved

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        if (zoomOn) {
            mousePoint = evt.getPoint();
            repaint();
        }
    }//GEN-LAST:event_formMouseMoved

    private void formMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseExited
        if (zoomOn) {
            mousePoint = null;
            repaint();
        }
    }//GEN-LAST:event_formMouseExited

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        createGraphicsBuffers();
    }//GEN-LAST:event_formComponentShown

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void setLearningRule(LMS learningRule) {
        this.learningRule = learningRule;
    }

    public void pauseLearning() {
        this.learningRule.pause();
    }

    public void resumeLearning() {
        this.learningRule.resume();
    }

       // ISSUE: drawing thread is late after graph scaling is done
    // ad tripl ebuffering so when i reach end, existing pael is replaced with new so it does not require redrawing from begining
    // the back bufferr is then cloned and resized in background
    @Override
    public void update(Observable o, Object arg) {         
        //this.learningRule = learningRule = (LMS) o;

        if (learningRule.isPausedLearning()) return;

//        learningRule.pause();

        LearningInfo learningInfo = new LearningInfo(   learningRule.getCurrentIteration(),
                                                        learningRule.getTotalNetworkError());

        Point2D point = new Point2D.Double(learningInfo.getIteration(), learningInfo.getError());
        addPoint(point);
        pointsQueueBuffer.add(point);
      //   worker.setHasData(true);

        bufferCounter++;
        if (bufferCounter > 10) {
          bufferCounter = 0;
          learningRule.pause();
          worker.setHasData(true);
        }

        if (learningRule.getCurrentIteration() == 1) prevPoint = null;// line drawing hack

//        worker.setData(learningInfo);

   //     learningRule.resume();

        if (learningRule.isStopped()) {
            learningRule.deleteObserver(this);
            this.redrawDrawingBuffer();
            this.repaint();
        } 
    }



    public Vector<Point2D> getPointsBuffer() {
        return pointsBuffer;
    }    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

            private class LearningInfo {
            Integer iteration;
            Double error;


            public LearningInfo(Integer iteration, Double error) {
                this.iteration = iteration;
                this.error = error;
            }

            public Double getError() {
                return error;
            }

            public Integer getIteration() {
                return iteration;
            }

        }

    private class TrainerSwingWorker extends SwingWorker<Void, Point2D>
    {
        boolean hasData = false;
        int counter = 0;
        boolean pause = false;

        public TrainerSwingWorker() {

        }

        public void setHasData(boolean hasData) {
            this.hasData = hasData;

            if (hasData == true) {
                    synchronized(this) {
                        notify(); // notify doInBackground to draw
                    }
            }
        }

	@Override
	protected Void doInBackground() throws Exception
	{
                while (true) {
                            //Wait until data is available.
                    synchronized(this) {
                            while (!hasData) {
                                try {
                                    wait();
                                } catch (InterruptedException e) {}
                            }
                    }

                    Point2D point = null;
                    while (!pointsQueueBuffer.isEmpty()) {
                        point = (Point2D)pointsQueueBuffer.poll();
                        publish(point);
                    }
                       
                     hasData = false;
                 }

	}

	@Override
	protected void process(List<Point2D> chunks)
	{
                    Point2D point = chunks.get(chunks.size()-1);
                
                    drawPoint(point);
                    repaint();
                    resumeLearning();


// used when setData waits for drawing to complete
//                    synchronized(this) {
//                        notify();
//                    }

                    // problem je sto se ucenje zavrsi pre nego iscrtavanje
                    // zbog tog atreba prtiti poslednju icsrtanu tacki iteracije pa ako nije zavrseno iscrtati sve do kraja
	}

}

}
