import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PasswordCracker implements Callable<String> {
	
	
	int intervalStart;
	int intervalEnd;
	String hash;

	public PasswordCracker(int intervalStart, int intervalEnd, String hash) {
		this.hash = hash;
		this.intervalStart = intervalStart;
		this.intervalEnd = intervalEnd;
	}
	
	
	public String call() throws Exception {

		for (int i = intervalStart; i < intervalEnd; i++) {
			if (sha256(i).equals(hash)) {
				System.out.println(i);
				System.exit(0);
			}
		}
		return null;
	}
	
	
	public static String sha256(int possiblePassword) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(("" + possiblePassword).getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();
			
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			
			return hexString.toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
	public static void main(String[] args) {

		
		int min = Integer.parseInt(args[0]);
		int max = Integer.parseInt(args[1]);
		int threadCount = 4;


		String hash = "6aed7c56a53e4bce1a208a52db4f460624733e8508d20ab8d59a568eb20e8113"; //pass: 15763453

		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		Collection<PasswordCracker> collection = new ArrayList<PasswordCracker>();

		PasswordCracker task = null;

		for (int i = 0; i < threadCount; i++) {
			int minInterval = min + i * ((max - min) / threadCount);
			int maxInterval = minInterval + ((max - min) / threadCount);
			task = new PasswordCracker(minInterval, maxInterval, hash);
			collection.add(task);
		}
		
		try {
			String string = executor.invokeAny(collection);
			System.out.println(string);
		} catch (Exception e) {
			e.printStackTrace();
		}
		executor.shutdown();
	}

	
	
}
