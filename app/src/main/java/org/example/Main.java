package org.example;



class DemoClass implements AutoCloseable{

    @Override
    public void close() throws Exception {
        System.out.println("closing resources");
    }

    @Override
    public String toString() {
        return "to string of demo";
    }
}
public class Main {


    public static void main(String[] args) throws Exception {
        try(var d = new DemoClass()){
            System.out.println(d);
        }
        //d.notifyAll();
//        ReenterExample.noidea(args);
//        ThreadCountExample.example(args);
    }
}
