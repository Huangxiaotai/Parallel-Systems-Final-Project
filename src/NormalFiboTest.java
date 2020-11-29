public class NormalFiboTest {
    public int normalFibo(int number) {
        if (number == 1 || number == 2) {
            return 1;
        }
        return normalFibo(number - 1) + normalFibo(number - 2);
    }

}
