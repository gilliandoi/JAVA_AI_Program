public class Matching {
	public static void main(String arg[]) {
		if (arg.length != 2) {
			System.out.println("Usgae : % Matching [String1] [String2]");
		} else {
			Matcher matcher = new Matcher();
			Boolean result = matcher.matching(arg[0], arg[1]);
			System.out.println(arg[0] + "," + arg[1] + "," + result);
		}
	}
}
