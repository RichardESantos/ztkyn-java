package org.gitee.ztkyn;

public class MainTest {

	public static void main(String[] args) {
		System.out.println(sum(10));
	}

	public static int sum(int num) {
		return num == 1 ? 1 : sum(num - 1) + num;
	}

}
