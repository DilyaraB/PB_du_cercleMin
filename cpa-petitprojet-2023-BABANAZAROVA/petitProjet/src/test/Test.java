package test;

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
import algorithms.DefaultTeam;

public class Test {
	public static ArrayList<Point> readPointsFromFile(String fileName) {
	      ArrayList<Point> points = new ArrayList<>();
	      try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
	          String line;
	          while ((line = br.readLine()) != null) {
	              String[] parts = line.split(" ");
	              int x = Integer.parseInt(parts[0]);
	              int y = Integer.parseInt(parts[1]);
	              points.add(new Point(x, y));
	          }
	      } catch (IOException e) {
	          e.printStackTrace();
	      }
	      return points;
	  }
	
	  public static void WriteCPUTimeBMDToFile(String filePath, String filename, String outputPath) {
		  ArrayList<Point> points = readPointsFromFile(filePath + filename);
	      try (FileWriter writer = new FileWriter(outputPath, true)) {
	          ArrayList<Point> bordure = new ArrayList<>();

	          long startTime = System.nanoTime();
	          DefaultTeam.b_md(points, bordure);
	          long endTime = System.nanoTime();

	          long duration = (endTime - startTime); //durée en nanosecondes
	          double durationInMilliseconds = duration / 1e6; //convertit en millisecondes

	          writer.write(filename + " " + durationInMilliseconds + " ms\n");
	          System.out.println(durationInMilliseconds + " ms");
	      } catch (IOException e) {
	          e.printStackTrace();
	      }
	  }
	  
	  
	  public static void WriteCPUTimeNaifToFile(String filePath, String filename, String outputPath) {
		  ArrayList<Point> points = readPointsFromFile(filePath + filename);
	      try (FileWriter writer = new FileWriter(outputPath, true)) {

	          long startTime = System.nanoTime();
	          DefaultTeam.cercleMinNaif(points);
	          long endTime = System.nanoTime();

	          long duration = (endTime - startTime); //durée en nanosecondes
	          double durationInMilliseconds = duration / 1e6; //convertit en millisecondes

	          writer.write(filename + " " + durationInMilliseconds + " ms\n");
	          System.out.println(durationInMilliseconds + " ms");
	      } catch (IOException e) {
	          e.printStackTrace();
	      }
	  }
	  
	  public static void WriteMemoryUsageNaifToFile(String filePath, String filename, String outputMemoryPath) {
	      ArrayList<Point> points = readPointsFromFile(filePath + filename);

	      try (FileWriter memoryWriter = new FileWriter(outputMemoryPath, true)) {

	          //calcul de la mémoire utilisée
	          Runtime runtime = Runtime.getRuntime();
	          runtime.gc(); // appel du garbage collector pour une mesure plus précise
	          long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
	          DefaultTeam.cercleMinNaif(points);
	          long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
	          long memoryUsed = memoryAfter - memoryBefore;

	          memoryWriter.write(filename + " " + memoryUsed + " bytes\n");

	          System.out.println("Memory Usage = " + memoryUsed + " bytes");
	          
	      }catch (IOException e) {
	          e.printStackTrace();
	      }
	  }
	  
	  public static void WriteMemoryUsageBMDToFile(String filePath, String filename, String outputMemoryPath) {
	      ArrayList<Point> points = readPointsFromFile(filePath + filename);

	      try (FileWriter memoryWriter = new FileWriter(outputMemoryPath, true)) {

	          //calcul de la mémoire utilisée
	          Runtime runtime = Runtime.getRuntime();
	          runtime.gc(); // appel du garbage collector pour une mesure plus précise
	          long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
	          DefaultTeam.b_md(points, new ArrayList<Point>());
	          long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
	          long memoryUsed = memoryAfter - memoryBefore;

	          memoryWriter.write(filename + " " + memoryUsed + " bytes\n");

	          System.out.println("Memory Usage = " + memoryUsed + " bytes");
	          
	      }catch (IOException e) {
	          e.printStackTrace();
	      }
	  }
	  
	  public static void calculateCPUFilesFromDirectory(String directoryPath, String outputPathNaif,String outputPathBMD ) {
	      File dir = new File(directoryPath);

	      //filtre qui sélectionne que les fichiers qui correspondent au motif "test-*.points"
	      FilenameFilter filter = (dir1, name) -> name.matches("test-\\d+\\.points");

	      String[] files = dir.list(filter);

	      if (files != null) {
	          //trie les fichiers par ordre numérique
	          Arrays.sort(files, (f1, f2) -> {
	              int num1 = Integer.parseInt(f1.replace("test-", "").replace(".points", ""));
	              int num2 = Integer.parseInt(f2.replace("test-", "").replace(".points", ""));
	              return Integer.compare(num1, num2);
	          });

	          //itére sur chaque fichier, en excluant "test-1.points"
	          for (String fileName : files) {
	              int fileNumber = Integer.parseInt(fileName.replace("test-", "").replace(".points", ""));
	              if (fileNumber > 1) {
	                  System.out.println("Processing " + fileName);
	                  
	                  WriteCPUTimeBMDToFile(directoryPath, fileName, outputPathBMD);
	                  WriteCPUTimeNaifToFile(directoryPath, fileName, outputPathNaif);
	                  
//	                  WriteMemoryUsageNaifToFile(directoryPath, fileName, outputPathNaif);
//	                  WriteMemoryUsageBMDToFile(directoryPath, fileName, outputPathBMD);
	              }
	          }
	      } else {
	          System.out.println("Pas de fichier trouvé correspondant au filtre");
	      }
	  }
	  
	  
	  public static void calculateMemoryFilesFromDirectory(String directoryPath, String outputPathNaif,String outputPathBMD ) {
	      File dir = new File(directoryPath);

	      //filtre qui sélectionne que les fichiers qui correspondent au motif "test-*.points"
	      FilenameFilter filter = (dir1, name) -> name.matches("test-\\d+\\.points");

	      String[] files = dir.list(filter);

	      if (files != null) {
	          //trie les fichiers par ordre numérique
	          Arrays.sort(files, (f1, f2) -> {
	              int num1 = Integer.parseInt(f1.replace("test-", "").replace(".points", ""));
	              int num2 = Integer.parseInt(f2.replace("test-", "").replace(".points", ""));
	              return Integer.compare(num1, num2);
	          });

	          //itére sur chaque fichier, en excluant "test-1.points"
	          for (String fileName : files) {
	              int fileNumber = Integer.parseInt(fileName.replace("test-", "").replace(".points", ""));
	              if (fileNumber > 1) {
	                  System.out.println("Processing " + fileName);
	                  
	                  WriteMemoryUsageNaifToFile(directoryPath, fileName, outputPathNaif);
	                  WriteMemoryUsageBMDToFile(directoryPath, fileName, outputPathBMD);
	              }
	          }
	      } else {
	          System.out.println("Pas de fichier trouvé correspondant au filtre");
	      }
	  }
	  
	  
	  public static void calculerMoyenneEcartFrequenceTempsCPU(String fichierEntree, String fichierSortieMoyenne, String fichierSortieEcart, String fichierSortieFreq) {
	      try (BufferedReader reader = new BufferedReader(new FileReader(fichierEntree));
	           FileWriter writer = new FileWriter(fichierSortieMoyenne)) {
	          String ligne;
	          double sommeTemps = 0;
	          int nombreDeValeurs = 0;
	          ArrayList<Double> tempsMesures = new ArrayList<>();
	          Map<Double, Integer> frequences = new HashMap<>();

	          while ((ligne = reader.readLine()) != null) {
	              String[] parties = ligne.split(" ");
	              String tempsEnMs = parties[1];
	              double temps = Double.parseDouble(tempsEnMs);
	              tempsMesures.add(temps);
	              frequences.put(temps, frequences.getOrDefault(temps, 0) + 1);
	              sommeTemps += temps;
	              nombreDeValeurs++;
	          }

	          double moyenne = nombreDeValeurs > 0 ? sommeTemps / nombreDeValeurs : 0;
	          writer.write(moyenne + " ms\n");
	          System.out.println("Moyenne des temps calculée et écrite dans " + fichierSortieMoyenne);
	          
	          double sommeDesEcartCarres = 0;

	          for (double temps : tempsMesures) {
	              sommeDesEcartCarres += Math.pow(temps - moyenne, 2);
	          }
	          
	          double ecartType = Math.sqrt(sommeDesEcartCarres / nombreDeValeurs);
	          
	          try (FileWriter writer2 = new FileWriter(fichierSortieEcart)) {
	              writer2.write(ecartType + " ms\n");
	              System.out.println("Écart type des temps calculé et écrit dans " + fichierSortieEcart);
	          }
	          
	          ArrayList<Double> sortedKeys = new ArrayList<>(frequences.keySet());
	          Collections.sort(sortedKeys);
	          
	          try (FileWriter writer3 = new FileWriter(fichierSortieFreq)) {
	        	  for (double key : sortedKeys) {
	                  writer3.write(key + " ms: " + frequences.get(key) + "\n");
	        	  }
	              System.out.println("Données du diagramme de fréquences écrites dans " + fichierSortieFreq);

	          }

	      } catch (IOException e) {
	          e.printStackTrace();
	      }
	  }
	  
	  
	  public static void calculerMoyenneMemoire(String fichierEntree, String fichierSortie) {
	      try (BufferedReader reader = new BufferedReader(new FileReader(fichierEntree));
	           FileWriter writer = new FileWriter(fichierSortie)) {
	          String ligne;
	          long sommeMemoire = 0;
	          int nombreDeValeurs = 0;

	          while ((ligne = reader.readLine()) != null) {
	              String[] parties = ligne.split(" ");
	              if (parties.length == 3) {
	                  String memoireEnBytes = parties[1];
	                  long memoire = Long.parseLong(memoireEnBytes);
	                  sommeMemoire += memoire;
	                  nombreDeValeurs++;
	              }
	          }

	          double moyenne = nombreDeValeurs > 0 ? (double)sommeMemoire / nombreDeValeurs : 0;
	          writer.write(String.format("%.2f bytes\n", moyenne));
	          System.out.println("Moyenne mémoire calculée et écrite dans " + fichierSortie);

	      } catch (IOException e) {
	          e.printStackTrace();
	      }
	  }
	  

	  public static void main(String[] args) {
		  
	      
		  String testsDirectoryPath = System.getProperty("user.dir") + "/TME1_cpa2023/Varoumas_benchmark/samples/";
		  System.out.println(testsDirectoryPath);

		  String filePathTimeNaif = System.getProperty("user.dir") + "/temps_cpu_naif.txt";
	      String filePathTimeBMD = System.getProperty("user.dir") +"/temps_cpu_bmd.txt";
	      
	      String filePathMemoryNaif = System.getProperty("user.dir") + "/memory_naif.txt";
	      String filePathMemoryBMD = System.getProperty("user.dir") + "/memory_bmd.txt";
	      
	      String filePathTimeNaifMean = System.getProperty("user.dir") + "/temps_cpu_naif_mean.txt";
		  String filePathTimeBMDMean = System.getProperty("user.dir") + "/temps_cpu_bmd_mean.txt";
		  
		  String filePathTimeNaifEcart = System.getProperty("user.dir") +"/temps_cpu_naif_ecart.txt";
		  String filePathTimeBMDEcart = System.getProperty("user.dir") +"/temps_cpu_bmd_ecart.txt";
		  
		  String filePathTimeNaifFreq = System.getProperty("user.dir") +"/temps_cpu_naif_freq.txt";
		  String filePathTimeBMDFreq = System.getProperty("user.dir") +"//temps_cpu_bmd_freq.txt";
		  
		  String filePathMemoryNaifMean = System.getProperty("user.dir") +"/memory_naif_mean.txt";
		  String filePathMemoryBMDMean = System.getProperty("user.dir") +"/memory_bmd_mean.txt";

		  
	      calculateCPUFilesFromDirectory(testsDirectoryPath, filePathTimeNaif, filePathTimeBMD);
	      calculateMemoryFilesFromDirectory(testsDirectoryPath, filePathMemoryNaif, filePathMemoryBMD);
	      
		  calculerMoyenneEcartFrequenceTempsCPU(filePathTimeNaif, filePathTimeNaifMean, filePathTimeNaifEcart, filePathTimeNaifFreq);
		  calculerMoyenneEcartFrequenceTempsCPU(filePathTimeBMD, filePathTimeBMDMean, filePathTimeBMDEcart, filePathTimeBMDFreq);

	      calculerMoyenneMemoire(filePathMemoryNaif, filePathMemoryNaifMean);
	      calculerMoyenneMemoire(filePathMemoryBMD, filePathMemoryBMDMean);

	  }
}
