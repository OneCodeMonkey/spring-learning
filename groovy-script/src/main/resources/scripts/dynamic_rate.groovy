package scripts;

int decideQps(int qps) {
    println("current qps is: " + qps);

    if (qps <= 15) {
        return 100;
    } else if (qps <= 20) {
        return 70;
    } else if (qps <= 25) {
        return 30;
    } else if (qps <= 30) {
        return 20;
    } else {
        return 0;
    }
}
