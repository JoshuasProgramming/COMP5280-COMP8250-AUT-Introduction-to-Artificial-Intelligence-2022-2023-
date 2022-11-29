//use this kNN function as an example template 

import java.lang.Math;
import java.util.*;
import java.util.stream.DoubleStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class kNN_simple 
{
 public static void main(String[] args) throws IOException 
 {
 
     int TRAIN_SIZE=30; //no. training patterns 
     int VAL_SIZE=30; //no. testing patterns 
     int FEATURE_SIZE=4; //no. of features

     double[][] train = new double [TRAIN_SIZE][FEATURE_SIZE]; //training data 
     double[][] val = new double[VAL_SIZE][FEATURE_SIZE]; //validation data
     int[] train_label= new int [TRAIN_SIZE]; //actual target/class label for train data
     int[] val_label= new int [VAL_SIZE]; //actual target/class label for validation data 

     String train_file="iris_train_data.txt"; //read training data
     try (Scanner tmp = new Scanner(new File(train_file))) {
        for (int i=0; i<TRAIN_SIZE; i++) {
            for (int j=0; j<FEATURE_SIZE; j++) {
                if(tmp.hasNextDouble()) {
                    train[i][j]=tmp.nextDouble();
                    //System.out.print(train[i][j] + " ");
                }
            }
        }
        tmp.close();
     }

     String val_file="iris_val_data.txt"; //read training data
     try (Scanner tmp = new Scanner(new File(val_file))) {
        for (int i=0; i<VAL_SIZE; i++) {
            for (int j=0; j<FEATURE_SIZE; j++) {
                if(tmp.hasNextDouble()) {
                    val[i][j]=tmp.nextDouble();
                    //System.out.print(train[i][j] + " ");
                }
            }
        }
        tmp.close();
     }

     
     String train_label_file="iris_train_label.txt"; //read train label
    try (Scanner tmp = new Scanner(new File(train_label_file))) {
        for (int i=0; i<TRAIN_SIZE; i++) {
            if(tmp.hasNextInt()) {
                train_label[i]=tmp.nextInt();
                //System.out.print(train_label[i] + " ");
            }
        }
        tmp.close();
    }

    String val_label_file="iris_val_label.txt"; //read train label
    try (Scanner tmp = new Scanner(new File(val_label_file))) {
        for (int i=0; i<VAL_SIZE; i++) {
            if(tmp.hasNextInt()) {
                val_label[i]=tmp.nextInt();
                //System.out.print(train_label[i] + " ");
            }
        }
        tmp.close();
    }

      
     System.out.println("Distance(train,val):");
  
     double[][] dist_label = new double[TRAIN_SIZE][2]; //distance array, no of columns is increased by 1 to accommodate distance
     double[] y = new double[FEATURE_SIZE]; //temp variable for validation data
     double[] x = new double[FEATURE_SIZE]; //temp variable for train data
     
     int NUM_NEIGHBOR = 1; //k value in kNN
    //  int[] neighbor = new int[NUM_NEIGHBOR];
     int[] predicted_class = new int[VAL_SIZE];
     double[] accuracyList = new double[25];
         

    for(int c = 1; c < accuracyList.length; c++){
      NUM_NEIGHBOR = c;

      int[] neighbor = new int[NUM_NEIGHBOR];

       for (int j=0; j<VAL_SIZE; j++) //for every validation data
       {
          for (int f=0; f<FEATURE_SIZE; f++)
          y[f]=val[j][f]; 

            for (int i=0; i<TRAIN_SIZE; i++)
            {
              for (int f=0; f<FEATURE_SIZE; f++)
              x[f]=train[i][f]; 
       
            double sum=0.0;
            for (int f=0; f<FEATURE_SIZE; f++)
            sum=sum + ((x[f]-y[f])*(x[f]-y[f])); //Euclidean distance
       
            dist_label[i][0] = Math.sqrt(sum); //Euclidean distance
            dist_label[i][1] = train_label[i]; //add the target label
       
            System.out.println(dist_label[i][0] + " " + dist_label[i][1]);
            }
   
          Sort(dist_label,1); //sorting
    
          System.out.println();
          System.out.println("After sorting");
          for(int i = 0; i< TRAIN_SIZE; i++) 
          {
            for (int k = 0; k < 2; k++)
            System.out.print(dist_label[i][k] + " ");
            System.out.println();
          }
          System.out.println("NUM_NEIGHBOR value: " + NUM_NEIGHBOR);
          
          for (int n=0; n<NUM_NEIGHBOR; n++) //training label from required neighbors
          neighbor[n]=(int) dist_label[n][1];

          System.out.println();
          System.out.println("Neighbors after sorting");
        
          for(int n = 0; n<NUM_NEIGHBOR; n++) 
            System.out.print(neighbor[n] + " ");
     
          System.out.println();
        
        predicted_class[j]=Mode(neighbor);
        System.out.print("Predicted class = " + predicted_class[j]);
        System.out.println(); System.out.println();
     
        } //end test data loop
       
       //accuracy computation 
       //only if labels are provided, eg for validation data
       //disable if using test data 
       int success=0;
       for (int j=0; j<VAL_SIZE; j++)
        if (predicted_class[j]==val_label[j])
        success=success+1;
       double accuracy=(success*100.0)/VAL_SIZE;

       accuracyList[c] = accuracy;
      //  System.out.print("Accuracy = " + accuracy);
    
       //writing kNN_output.txt in the required format 
      }

      


      for (int d = 0; d <accuracyList.length; d++) {

        System.out.println("Index: " + d + "\n Accuracy: " + accuracyList[d]);

      }

      //write a loop finding the maximum value of accuracyList, and printing its index
      double[] accuracyList_Copy = accuracyList.clone();

      //sort double array
      Arrays.sort(accuracyList_Copy);

      double max = accuracyList_Copy[0];
      
      int maxIndex = -1;

      for (int g = 0; g < accuracyList_Copy.length; g++) {

          double value = accuracyList[g];

          if (value > max) {
              max = value;
              maxIndex = g;
          }
      }
      
      System.out.println("Maximum value: " + max + "\n Index: " + maxIndex);
      
      // System.out.println(accuracyList.toString());
      try
      {
      PrintWriter writer = new PrintWriter("kNN_output.txt", "UTF-8");
        for(int j=0; j<VAL_SIZE; j++) 
        writer.print(predicted_class[j] + " ");
      writer.close();
      }
      catch(Exception e)
      {
      System.out.println(e);
      }

  } //end main loop

 
 public static void Sort (double[][] sort_array, final int column_sort) //sorting function
 {
 Arrays.sort(sort_array, new Comparator<double[]>() 
  {
    @Override
    public int compare(double[] a, double[] b) 
    {
    if(a[column_sort-1] > b[column_sort-1]) return 1;
      else return -1;
    }
  });
}
     
 
 public static int Mode(int neigh[]) //function to find mode
 {
  int modeVal=0;
  int maxCnt=0;
     
  for (int i = 0; i < neigh.length; ++i) 
    {
    int count = 0;
      for (int j = 0; j < neigh.length; ++j)
      {
        if (neigh[j] == neigh[i]) 
        count=count+1;
      }
        if (count > maxCnt) 
        {
        maxCnt = count;
        modeVal = neigh[i];
        }
    }

  return modeVal;
 }
 
 
} //end class loop