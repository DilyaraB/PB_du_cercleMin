package algorithms;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import supportGUI.Circle;

public class DefaultTeam {
	
  public static Random random = new Random();

  
  public Circle calculCercleMin(ArrayList<Point> points) {
	  //return cercleMinNaif(new ArrayList<Point>(points));
	  Circle disk = b_md(new ArrayList<Point>(points), new ArrayList<Point>());
	  System.out.println("radius de cercle bmd :"+ disk.getRadius());
	  return disk;
  }
  
  
  
 public static Circle cercleMinNaif(ArrayList<Point> inputpoints){
      ArrayList<Point> points = new ArrayList<Point>(inputpoints);
      if (points.size()<1) {
    	  return null;
      }
      double centreX,centreY,cRayon;
      boolean pointsDedans;
      
      for (Point p: points){
          for (Point q: points){
        	  centreX = (p.x + q.x)/2;
        	  centreY = (p.y + q.y)/2;
        	  cRayon = ((p.x - q.x)*(p.x - q.x)+(p.y - q.y)*(p.y - q.y))/4;
              pointsDedans = true; 
              
              //vérifie si tous les points sont dans le cercle
              for (Point s: points) {
            	  double distCentreK = (s.x - centreX)*(s.x - centreX)+(s.y - centreY)*(s.y - centreY);
                  if ( distCentreK > cRayon){
                	  pointsDedans = false;
                      break;
                  }
              }
              
              if (pointsDedans) {
            	  return new Circle(new Point((int)centreX, (int)centreY), (int)Math.sqrt(cRayon));
              }
          }
      }
      
      double resX=0;
      double resY=0;
      double resRayon=Double.MAX_VALUE;
      
      for (int i=0; i<points.size(); i++){
          for (int j=i+1; j<points.size(); j++){
              for (int k=j+1; k<points.size(); k++){
            	  
                  Point p = points.get(i);
                  Point q = points.get(j);
                  Point r = points.get(k);
                  
                  //3 points colinéaires
                  if ((q.x - p.x) * (r.y - p.y) - (q.y - p.y) * (r.x - p.x) == 0) {
                	  continue;
                  }
                  
                  if ((p.y == q.y) || (p.y == r.y)) {
                      if (p.y == q.y){
                          p = points.get(k);
                          r = points.get(i);
                      } else {
                          p = points.get(j);
                          q = points.get(i);
                      }
                  }
                  //calcul des coordonnees du cercle circonscrit des trois points pqr
                  double centrepqX = (p.x + q.x)/2;
                  double centrepqY = (p.y + q.y)/2;
                  double centreprX = (p.x + r.x)/2;
                  double centreprY = (p.y + r.y)/2;
                  
                  //droites passant par centrepq et perpendiculaire à pq, pareil pour centrepr
                  double a1 = (q.x - p.x) / (double)(p.y - q.y);
                  double b1 = centrepqY - a1 * centrepqX;
                  double a2 = (r.x - p.x)/(double)(p.y - r.y);
                  double b2 = centreprY - a2*centreprX;
                  
                  //centre c calculé à partir du points d'intersection des deux droites
                  centreX = (b2 - b1)/(double)(a1 - a2);
                  centreY = a1*centreX + b1;
                  cRayon = (p.x - centreX)*(p.x - centreX)+(p.y - centreY)*(p.y - centreY);
                  
                  if (cRayon>=resRayon) {
                	  continue;
                  }
                  pointsDedans = true;
                  
                  //vérifie si tous les points sont dans le cercle
                  for (Point s: points) {
                	  double distCentreK = (s.x - centreX)*(s.x - centreX)+(s.y - centreY)*(s.y -  centreY);
                      if (distCentreK > cRayon){
                    	  pointsDedans = false;
                          break;
                      }
                  }
               
                  if (pointsDedans) {
                	  resX = centreX;
                	  resY = centreY;
                	  resRayon = cRayon;
                  }
              }
          }
      }
      Circle res = new Circle(new Point((int)resX, (int)resY), (int)Math.sqrt(resRayon));
	  System.out.println("radius de cercle naif :"+ res.getRadius());
      return res;
  }
  
  
  
  
 public static Circle b_md(ArrayList<Point> points, ArrayList<Point> bordure) {
	  
	  Circle disk;
	  if (points.size()== 0 || bordure.size()==3) {
		  if (bordure.size() == 3) {
	            disk = cercle3Points(bordure.get(0), bordure.get(1), bordure.get(2));
	        } else {
	            disk = new Circle(new Point(0, 0), 0); //par défaut, si aucun points
	        }
	  }
	  else {
		  ArrayList<Point> tmpPoints = new ArrayList<Point>(points);
		  Point p = tmpPoints.get(random.nextInt(tmpPoints.size()));
		  tmpPoints.remove(p);
		  disk = b_md (tmpPoints, bordure);
		  if (disk != null) {
			  Point center = disk.getCenter();
			  double radius = disk.getRadius();
			  
			  //vérifie si p est à l'extérieur de disk
			  if (center.distance(p) > radius) {
				  ArrayList<Point> tmpBordure = new ArrayList<Point>(bordure);
				  tmpBordure.add(p);
				  disk = b_md(tmpPoints, tmpBordure);
			  }
		  } 
	  } 
     
	  return disk;
  }
  
  
  private static Circle cercle3Points(Point p1, Point p2, Point p3) {
	  //par optimisation, on devrait faire le calcul de distance à la main
      double distP1P2 = p1.distance(p2);
      double distP2P3 = p2.distance(p3);
      double distP3P1 = p3.distance(p1);

      //vérifie si les points sont alignés
      double determinant = p1.x * (p2.y - p3.y) + p2.x * (p3.y - p1.y) + p3.x * (p1.y - p2.y);
      if (Math.abs(determinant) < 1e-9) {
          //points alignés, retourne le cercle avec le centre au milieu du segment le plus long
          Point center;
          double radius;
          if (distP1P2 >= distP2P3 && distP1P2 >= distP3P1) {
              center = new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
              radius = distP1P2 / 2;
          } else if (distP2P3 >= distP1P2 && distP2P3 >= distP3P1) {
              center = new Point((p2.x + p3.x) / 2, (p2.y + p3.y) / 2);
              radius = distP2P3 / 2;
          } else {
              center = new Point((p3.x + p1.x) / 2, (p3.y + p1.y) / 2);
              radius = distP3P1 / 2;
          }
          return new Circle(center, (int)radius);
      }

      //calcul des coordonnées du centre du cercle
      double a = p2.x - p1.x;
      double b = p2.y - p1.y;
      double c = p3.x - p1.x;
      double d = p3.y - p1.y;
      double e = a * (p1.x + p2.x) + b * (p1.y + p2.y);
      double f = c * (p1.x + p3.x) + d * (p1.y + p3.y);
      double g = 2 * (a * (p3.y - p2.y) - b * (p3.x - p2.x));
      
      double centerX = (d * e - b * f) / g;
      double centerY = (a * f - c * e) / g;

      //calcul du rayon du cercle
      Point newPoint = new Point((int)centerX, (int)centerY);
      double radius = p1.distance(newPoint);

      Circle disk = new Circle(newPoint, (int)radius);
      return disk;
  }

}
