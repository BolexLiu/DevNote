package com.example;

public class MyClass {
    public static void main (String[] args) throws java.lang.Exception{
        Thread t =new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.print("pong");
            }
        });
        t.start();
        System.out.print("ping");

//        Dervied dervied = new Dervied("dervied");
//        dervied.pringname();
//        dervied.pringhelloA();

//        System.out.println("start");
//       new HelloB();
//        System.out.println("end");
    }

}

class HelloS {

    public HelloS() {
        System.out.println("HelloA");
    }

    { System.out.println("I'm A class"); }

    static { System.out.println("static A"); }

}

class HelloB extends HelloS {
    public HelloB() {
        System.out.println("HelloB");
    }

    { System.out.println("I'm B class"); }

    static { System.out.println("static B"); }



}

class HelloA{
    String name;
    public HelloA(String name){
        this.name=name;
    }

  public void  pringname() {
      System.out.println("myName:"+name);
    }
    public void  pringhelloA() {
        System.out.println("helloA啦");
    }

}
class Dervied extends HelloA{


    public Dervied(String name) {
        super(name);
    }
    public void  pringname() {
        System.out.println("myName:"+name);
    }
    public void  pringhelloA() {
        System.out.println("Dervied");
    }

}


