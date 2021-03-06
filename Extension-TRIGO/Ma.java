// Grenoble INP - Ensimag projet GL -*- mode: java -*-
// Library for class Math of Deca, coded in Deca

class Ma {
    // Declare global variables.
    float  PI = 3.14159265358979323846264338327950288419716939937510582f;


   // useful functions [power and square root]
    float sqrt(float f){
        // The Newton method to approfimate square root of f
        float error = 0.000001f;
        float guess = f;
        float newGuess = 0.0f;
        float difference = 100.3f;
        if (f == 0){
            return 0.0f;
        }
        while (difference > error){
            newGuess = guess - (guess*guess - f)/(2*guess);
            difference = newGuess - guess;
            if (difference < 0){
                difference = -difference;
            }
            guess = newGuess;
        }
        return guess;
    }

    int minusPow(int n){
        if (n%2 == 0){
            return 1;
        }
        return -1;
    }

    // puissance
    float pow(float f, int n){
       float result = f;
       int i = 1;
       if (n < 0){
            return pow(1/f, -n);
        } else{
           while( i < n){
               result = result*f;
               i = i + 1;
           }
       }
       return result;
   }

   /*float pow(float f, int n){
       float result = 1;
       int i = 1;
       if (n < 0){
           result = 1/f;
           i = i + 1;
           f = 1/f;
           n = -n;
       }
       while( i <= n){
               result = result*f;
               i = i + 1;
       }
       return result;
    }*/

    // Valeur absolue
    float abs(float f){
        if (f < 0){
            f = -f;
        }
        return f;
    }

    // factoriel d'un entier
    int fact(int n){
        int m = n;
        int p = 1;
        while(m>1){
            p = p*m;
            m = m-1;
        }
        return p;
    }

    float sin(float f) {

        float p0 = 1.57079632662187f;
        float p1 = -0.645964092652696f;
        float p2 = 0.079692587335023f;
        float p3 = -0.004681620350771f;
        float p4 = 0.000160217246309f;
        float p5 = -0.000003418213039f;


        int n = (int) (f /(PI/2));
        float c1 = 50*pow(2,-5);
        float c2 = 16*pow(2,-11);
        float c3 = 63*pow(2,-17);
        float c4 = 26*pow(2,-23);
        float c5 = 40*pow(2,-29);
        float c6 = 34*pow(2,-35);
        float c7 = 5*pow(2,-41);
        float c8 = 40*pow(2,-47);
        float c9 = 48*pow(2,-53);
        float c10 = 35*pow(2,-59);
        float c11 = 19*pow(2,-65);
        float c12 = 4*pow(2,-71);
        float c13 = 49*pow(2,-77);
        float c14 = 38*pow(2,-83);
        float c15 = 10*pow(2,-89);
        float c16 = 11*pow(2,-95);

        if (0<=f && f<= PI/2) {
            return ((2 / PI) * f * (p5*pow((2/PI)*f,10)+p4*pow((2/PI)*f,8)+p3 * pow((2 / PI) * f, 6) + p2 * pow((2 / PI) * f, 4) + p1 * pow((2 / PI) * f, 2) + p0));

        }
        else if (f<0){
            return -sin(-f);
        }
        else{

            f = f - n*c1;
            f = f - n*c2;
            f = f - n*c3;
            f = f - n*c4;
            f = f - n*c5;
            f = f - n*c6;
            f = f - n*c7;
            f = f - n*c8;
            f = f - n*c9;
            f = f - n*c10;
            f = f - n*c11;
            f = f - n*c12;
            f = f - n*c13;
            f = f - n*c14;
            f = f - n*c15;
            f = f - n*c16;
            if (n%4 == 1){
                return cos(f);
            }
            else if (n%4 == 2){
                return -sin(f);
            }
            else if (n%4 == 3){
                return -cos(f);
            }
            else  {
                return sin(f);
            }
        }
    }


    float cos(float f) {
        float p0 = 0.999999953464f;
        float p1 = -0.499999053455f;
        float p2 = 0.0416635846769f;
        float p3 = -0.0013853704264f;
        float p4 = 0.00002315393167f;

        int n = (int) (f /(PI/2));
        float c1 = 50*pow(2,-5);
        float c2 = 16*pow(2,-11);
        float c3 = 63*pow(2,-17);
        float c4 = 26*pow(2,-23);
        float c5 = 40*pow(2,-29);
        float c6 = 34*pow(2,-35);
        float c7 = 5*pow(2,-41);
        float c8 = 40*pow(2,-47);
        float c9 = 48*pow(2,-53);
        float c10 = 35*pow(2,-59);
        float c11 = 19*pow(2,-65);
        float c12 = 4*pow(2,-71);
        float c13 = 49*pow(2,-77);
        float c14 = 38*pow(2,-83);
        float c15 = 10*pow(2,-89);
        float c16 = 11*pow(2,-95);

        if (0<=f && f<=PI/2) {
            return ((((p4 * pow(f, 2) + p3) * pow(f, 2) + p2) * pow(f, 2)) + p1) * pow(f, 2) + p0;
        }
        if (f<0){
            return cos(-f);
        }
        else{
        f = f - n*c1;
        f = f - n*c2;
        f = f - n*c3;
        f = f - n*c4;
        f = f - n*c5;
        f = f - n*c6;
        f = f - n*c7;
        f = f - n*c8;
        f = f - n*c9;
        f = f - n*c10;
        f = f - n*c11;
        f = f - n*c12;
        f = f - n*c13;
        f = f - n*c14;
        f = f - n*c15;
        f = f - n*c16;
        if (n%4 == 1){
            return -sin(f);
        }
        else if (n%4 == 2){
            return -cos(f);
        }
        else if (n%4 == 3){
            return sin(f);
        }
        else  {
            return cos(f);
        }
     }


 }


    float asin(float f) {
        if (f<=0.5){
          return 2*atan(f/(1+sqrt(1-pow(f,2))));
        }
        else{
          return PI/2 - 2*asin(sqrt((1-f)/2));
        }
 }

    float atan(float f) {
        int i = 0;
        float s = 0;
        if (f<0){
          return -atan(-f);
        }
        if (f>1){
          return PI/2 - atan(1/f);
        }
        while (i<100){
          s = s + (minusPow(i)/(2*i+1))*pow(f,2*i+1);
          i=i+1;
        }
        return s;
    }


    float ulp(float f) {
        float min = pow(10,-45);
        int exposant = 0;
        if (f == 0) {
            return min;
        }
        if (f<0){
            f = -f;
        }
        if (f>=1){
            exposant = exposant - 1;
            while(f>=1){
                  f = f/2 ;
                  exposant = exposant + 1;
            }
        }
        else if(f<1 && f!=0){
            while(f<1) {
                  f = f*2;
                  exposant = exposant - 1;
            }

        }
        return pow(2,exposant-23);
     }
}
