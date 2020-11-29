public class RunTest {
    public static void main(String[] args) {

        long startTime = 0;
        long endTime = 0;
        FiboTest test = new  FiboTest();
        try {
            startTime = System.nanoTime();

            test.run();
            endTime = System.nanoTime();

            System.out.println("Work stealing is" + (endTime - startTime));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        NormalFiboTest nortest = new NormalFiboTest();

        startTime = System.nanoTime();
        System.out.println(nortest.normalFibo(21));
        endTime = System.nanoTime();
        System.out.println("nor Fibo is" + (endTime - startTime));
    }
}
