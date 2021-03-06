package dmlab.main;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class MedoidsAlgorithm {

	public static ArrayList<Point> NormalFamesPAM(ArrayList<Point> dataSet, ArrayList<Point> k_Medoids)
	{
		//0. init
		ArrayList<Point> new_K_Medoids = new ArrayList<Point>();
		//1. assing procedure.
		for(int i=0; i<dataSet.size(); i++)
		{
			double min = Double.MAX_VALUE;
			int classLabel = -1;
			for(int j=0; j<k_Medoids.size(); j++)
			{
				double distance = EuDistanceCalc.CalculateDistance(k_Medoids.get(j), dataSet.get(i));
				if(distance < min)
				{
					classLabel = k_Medoids.get(j).getClassLabel();
					min = distance;
				}
			}
			dataSet.get(i).setClassLabel(classLabel);
		}
		//2. new Centroid Calculation.
		//call doFames fuction!. (input : cluster points, return new Medoids.)
		for(int i=0; i<k_Medoids.size(); i++)
		{
			ArrayList<Point> eachClusterPoints = new ArrayList<Point>();
			//클러스터셋뽑기.
			for(int j=0; j<dataSet.size(); j++)
			{
				if(dataSet.get(j).getClassLabel() == k_Medoids.get(i).getClassLabel())
				{
					eachClusterPoints.add(dataSet.get(j));
				}
			}	
			new_K_Medoids.add(FAMES.doFAMES(eachClusterPoints));
		}
		return new_K_Medoids;
	}
	
	/**]
	 * 
	 * @param dataSet : all data points
	 * @param medoids : init medoids points
	 * @return double : final medoids cost
	 */
	public static void NormalOriginalPAM(List<Point> dataSet, Point[] medoids) {
		List<Point> clusteredPoints;
	
		int iterations = 0;
		Point[] newMedoids = null; 
		
		do{
			iterations++;
			clusteredPoints = new ArrayList<Point>();
			
			for(Point medoid : medoids) {
				clusteredPoints.add(medoid);
			}
			
			//assing step.
			for(Point p : dataSet) {
				double minDistance = Double.MAX_VALUE;
				for(Point medoid : medoids) {
					if(!containsPoint(medoids, p)) {
						double distance = EuDistanceCalc.CalculateDistance(p, medoid);
						if(distance < minDistance) {
							minDistance = distance;
							p.setClassLabel(medoid.getClassLabel());
						}
					}
				}
				clusteredPoints.add(p);
			}
			
			if(iterations == 1) {
				Point[] oldMedoids = medoids.clone();
				newMedoids = calculateNewMedoids(clusteredPoints, oldMedoids);
			}
			else {
				newMedoids = calculateNewMedoids(clusteredPoints, newMedoids);
			}
			
			System.out.println("Number of Iterations: " + iterations);
		} while(!stopIterations(newMedoids,medoids));
		

	}
	
	
	private static boolean containsPoint(Point[] points, Point point) {
		for(Point p : points) {
			if(p == point) 
				return true;
		}
		
		return false;
	}
	
	private static  double getTotalCost(List<Point> clusteredPoints, Point[] newMedoids) {
		double totalCost = 0;
		
		for(Point point : clusteredPoints) {
			double cost = Double.MAX_VALUE;
			
			for(Point newMedoid : newMedoids) {
				double tempCost = EuDistanceCalc.CalculateDistance(point, newMedoid);
				if(tempCost < cost)
					cost = tempCost;
			}
			
			totalCost += cost;
		}
		
		return totalCost;
	}
	
	private static  Point[] calculateNewMedoids(List<Point> clusteredPoints, Point[] oldMedoids) {
		for(int j = 0; j < oldMedoids.length; j++) {
			double oldTotalCost = getTotalCost(clusteredPoints, oldMedoids);
			double newTotalCost = Double.MAX_VALUE;
			
			Point oriMedoid = oldMedoids[j];
			Point candidateMedoid = null;
			
			for(Point candidate : clusteredPoints) {
				oldMedoids[j] = candidate;
				double tempTotalCost = getTotalCost(clusteredPoints, oldMedoids);
				
				if(tempTotalCost < newTotalCost) {
					newTotalCost = tempTotalCost;
					candidateMedoid = candidate;
				}
			}
			
			if(newTotalCost < oldTotalCost) {
				oldMedoids[j] = candidateMedoid;
			}
			else {
				oldMedoids[j] = oriMedoid;
			}
		}
	
		return oldMedoids;
	}

	private static  boolean stopIterations(Point[] newMedoids, Point[] oldMedoids) {
		boolean stopIterations = true;
		
		for(int i = 0; i < newMedoids.length; i++){
			if(!containsPoint(newMedoids, oldMedoids[i])){
				stopIterations = false;
			}
			oldMedoids[i] = newMedoids[i];
			oldMedoids[i].setClassLabel(i);
		}
		
		return stopIterations;
	}
	
	
	
	/**
	 * Local PAM
	 * 
	 */
	public static Point getLocalMedoid(List<Point> clusterPoints) {
		double minTotalDistance = Double.MAX_VALUE;
		Point medoid = null;
		
		for(Point candidatePoint : clusterPoints){
			double TotalDistance = 0;
			
			for(Point clusterPoint : clusterPoints){
				TotalDistance += EuDistanceCalc.CalculateDistance(candidatePoint, clusterPoint);
			}
			if(TotalDistance < minTotalDistance){
				minTotalDistance = TotalDistance;
				medoid = candidatePoint;
			}
		}
		return medoid;
	}


	

}
