import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

class Solution
{
	static int K;
	static HashMap<String, Integer> player = new HashMap<>(); // name, idx
	static ArrayList<Integer>[] synergy = new ArrayList[200];
	static int[] visit;
	static boolean isAble;
	
	private static void bfs(int idx) {
		Queue<Integer> q = new LinkedList<>();
		q.add(idx);
		if (visit[idx] == -1) {
			visit[idx] = 1;
		} else 
			return;
		
		while (!q.isEmpty()) {
			int curIdx = q.poll();
			for (int i : synergy[curIdx]) {
				if (visit[i] != -1 && visit[i] == visit[curIdx]) {
					isAble = false;
					return;
				}
				
				if (visit[i] == -1) {
					visit[i] = visit[curIdx] == 1 ? 0 : 1;
					q.add(i);
				}
			}
		}
	}

	
	private static void init() {
		player.clear();
		for (int i = 0; i < synergy.length; i++)
			synergy[i].clear();
		isAble = true;
	}
	
	public static void main(String args[]) throws Exception
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine());
		for (int i = 0; i < synergy.length; i++) {
			synergy[i] = new ArrayList<>();
		}
		for(int test_case = 1; test_case <= T; test_case++)
		{
			init();
			
			K = Integer.parseInt(br.readLine());
			
			int idx = 0;
			
			for (int i = 0; i < K; i++) {
				StringTokenizer st = new StringTokenizer(br.readLine());
				String p1 = st.nextToken();
				String p2 = st.nextToken();
				
				int idx1, idx2;
				if (player.containsKey(p1)) {
					idx1 = player.get(p1);
				} else {
					idx1 = idx;
					player.put(p1, idx++);
				}
				
				if (player.containsKey(p2)) {
					idx2 = player.get(p2);
				} else {
					idx2 = idx;
					player.put(p2, idx++);
				}
				
				synergy[idx1].add(idx2);
				synergy[idx2].add(idx1);
			}
			
			visit = new int[200];
			Arrays.fill(visit, -1);
			
			for (int i = 0; i < idx; i++) {
				bfs(i);
				if (!isAble) {
					break;					
				}
			}
			
			if (isAble) {
				System.out.println("#"+test_case+" Yes");
			} else {
				System.out.println("#"+test_case+" No");
			}
		}
		
		
	}

	
}