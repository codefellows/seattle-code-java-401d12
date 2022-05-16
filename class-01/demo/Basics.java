import java.util.Arrays;

// java is strongly typed language

public class Basics{

public static void main(String[] args){
int int1 = 150;
long long1 = 1000L;
byte byte1 = (byte)127;
short short1 = (short)0xffff;
boolean boolean1= true;

int maxInt = Integer.MAX_VALUE;
short minShort = Short.MIN_VALUE;
// System.out.println("Maximum integer is: " + maxInt + minShort);

float float1 = 0.1F;
float float2 = 0.2f;
float float3 = float1 + float2;
boolean boolFloat = (float3 == 0.3f);
System.out.println("booleanFloat: " + boolFloat);
System.out.println("float3: " + float3);

double d = 0.1d;
double d2 = 0.2d;
double d3 = d + d2;
boolean boolDouble = (d3 == 0.3d);
System.out.println("booleanD: " + boolDouble);
System.out.println("double3: " + d3);

// char -> character.
  char char1 = 'a';
  char char2 = 'b';
  System.out.println("myChar + myChar2: " + char1 + char2);

//string
  String myString = "2";
  int int3 = Integer.parseInt(myString);
  String name = "alex";

  System.out.println(myString + " " + name);

  boolean areMyStringsEqual = (myString == name);
  System.out.println("areMyStringsEqual: " + areMyStringsEqual);
// overflow

int voerflowInt = Integer.MAX_VALUE;
int newInt = voerflowInt + 1;
System.out.println("What is int value: " + voerflowInt + newInt);

// array

int[] myIntArray = new int[10];
int[] myIntArr2 = {1,2,3,4,5};


System.out.println("myIntArray2: " + myIntArr2); // yuck! memory address
System.out.println("myIntArray2 (actually): " + Arrays.toString(myIntArr2));

// loops

// for
  for(int i=0; i < myIntArr2.length; i++){
    System.out.println("myIntArr2[i]: " + myIntArr2[i]);
    if(i < 2){
      System.out.println("Zorkie");
      continue;
    }
  }

  for (long anInt : myIntArr2)
    {
      System.out.println("myIntArr2[i] 2: " + anInt);
    }

//while

int j = 0;
while(j < myIntArr2.length) {
  // system out [i]
  j++;
}

// methods and arrays

int returnInt = testFunc(10);
System.out.println("returnINt: " + returnInt);
System.out.println("testFunc return is: " + testFunc(10));

}


  public static int testFunc(int myInt)
  {
    System.out.println("My Integer is: " + myInt);
    return myInt + 1;
  }

}