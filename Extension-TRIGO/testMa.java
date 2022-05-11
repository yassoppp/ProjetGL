import java.util.*;
public class testMa{


    public static void testCos(){
        Ma m = new Ma();
        System.out.println("test cos");
        System.out.println("cos(2.32f) = " + m.cos(2.32f));
        System.out.println("Math.cos(2.32f) = " + Math.cos(2.32));
        System.out.println("cos(100020) = " + m.cos(100020f));
        System.out.println("Math.cos(100020) = " + Math.cos(100020));
        System.out.println("cos(0.3) = " + m.cos(0.3f));
        System.out.println("Math.cos(0.3) = " + Math.cos(0.3));
    }

    public static void testSin(){
        Ma m = new Ma();
        System.out.println("---------------------------");
        System.out.println("test sin");
        System.out.println("sin(2.32f) = " + m.sin(2.32f));
        System.out.println("Math.sin(2.32f) = " + Math.sin(2.32));
        System.out.println("sin(100020) = " + m.sin(100020f));
        System.out.println("Math.sin(100020) = " + Math.sin(100020));
        System.out.println("sin(0.3) = " + m.sin(0.3f));
        System.out.println("Math.sin(0.3) = " + Math.sin(0.3));
    }

    public static void testAtan(){
        Ma m = new Ma();
        System.out.println("---------------------------");
        System.out.println("test atan");
        System.out.println("atan(10) = " + m.atan(10f));
        System.out.println("Math.atan(10) = " + Math.atan(10));
        System.out.println("atan(100020) = " + m.atan(100020f));
        System.out.println("Math.atan(100020) = " + Math.atan(100020));
        System.out.println("atan(0.3) = " + m.atan(0.3f));
        System.out.println("Math.atan(0.3) = " + Math.atan(0.3));
    }

   public static void testAsin(){
      Ma m = new Ma();
      System.out.println("---------------------------");
      System.out.println("test asin");
      System.out.println("asin(0.12) = " + m.asin(0.12f));
      System.out.println("Math.asin(0.12) = " + Math.asin(0.12));
      System.out.println("asin(0.498) = " + m.asin(0.498f));
      System.out.println("Math.asin(0.498) = " + Math.asin(0.498));
      System.out.println("asin(0.97) = " + m.asin(0.97f));
      System.out.println("Math.asin(0.97) = " + Math.asin(0.97));
  }

  public static void testUlp(){
      Ma m = new Ma();
      System.out.println("---------------------------");
      System.out.println("test ulp");
      System.out.println("ulp(1) = " + m.ulp(1));
      System.out.println("Math.ulp(1) = " + Math.ulp(1));
      System.out.println("ulp(509.67) = " + m.ulp(509.67f));
      System.out.println("Math.ulp(509.67) = " + Math.ulp(509.67));
      System.out.println("ulp(209) = " + m.ulp(209));
      System.out.println("Math.ulp(209) = " + Math.ulp(209));
  }



    public static void main(String[] args) {
        testCos();
        testSin();
        testAtan();
        testAsin();
        testUlp();

    }
}
