# SWExpertAcademy_D4_Java_7988

## SW Expert Academy D4 7988. 내전경기

### 1. 문제설명

출처: https://swexpertacademy.com/main/code/problem/problemDetail.do?contestProbId=AWvQZmdKUoEDFASy&categoryId=AWvQZmdKUoEDFASy&categoryType=CODE

input으로 시너지를 가진 사람들의 조합 `K`개가 들어온다. 공백으로 구분되어 두명씩 들어오며 모든 사람들 두 팀으로 나눌때 아무 시너지도 없도록 만들수 있다면 `Yes`를 출력, 그렇지 않다면 `No`를 출력하는 문제.

[입력]
> 첫 번째 줄에 테스트 케이스의 수 `T`가 주어진다.
> 각 테스트 케이스의 첫 번째 줄에는 시너지 효과를 일으키는 조합의 수 `K(1 ≤ K ≤ 100)`가 주어진다.
> 이어서 `K`개의 줄에 이어서 시너지 관계를 내는 두 명의 이름이 주어진다.
> 이름은 공백없이 주어지며 대소문자 알파벳과 `_` 로, `50`자 이하로 이루어져 있다.

[출력]
> 각 테스트 케이스마다 `#x`(`x`는 테스트케이스 번호를 의미하며 `1`부터 시작한다)를 출력하고,
> 각 테스트 케이스마다 내전이 가능하게 팀 분배하는 것이 가능하면 `Yes`, 불가능하면 `No`를 출력하라.

### 2. BFS를 이용한 조사 풀이

먼저 모든 사람들을 받아 `HashMap<String, Integer>` 에 저장한다. 이때 값으로 들어갈 `Integer`는 시너지를 가지는 리스트`ArrayList<Integer>[]`의 인덱스를 뜻한다. 모든 멤버의 이름과 그에대한 인덱스, 시너지를 갖는 사람들 리스트를 완성하면 BFS를 이용하여 검사한다.

`idx`를 사람들의 시너지리스트를 담은 `ArrayList<>[]`의 인덱스라고 할 때, 하나의 `idx`를 BFS에 input으로 넣어준다. input으로 들어온 `idx`에 대하여 큐에 넣고 탐색을 시작한다. 큐에서 하나의 인덱스를 꺼내어 그 인덱스의 시너지 리스트의 인원들을 순차적으로 뽑아 반대쪽 팀에 넣어준다. 넣기전에 이미 팀에 속해있고 그 팀이 큐에서 뽑았던 인원과 같은 팀이라면 시너지를 가지는 팀이기 때문에 플래그 `isAble = false` 로 만들고 탐색을 멈춘 후 `No`를 출력하게한다.

성공적으로 해당 `idx`에 대하여 탐색을 마쳤다면 다음 `idx`를 넣어 검사를 수행한다. 그 이유로는 예시로 `A B`, `C D`가 들어온 경우가 있다. `A`는 `B`와 시너지를 갖지만 `C, D`와는 시너지를 갖지 않고 위의 방법으로는 이 경우가 검사되지 않는다. 그렇기 때문에 시너지 리스트를 끝까지 검사하며 모든 사람들을 이용하여 팀을 완성하였고 그렇게 하여서 가능한지 알기위해 모든 `idx`를 넣어 검사한다. 모든 조건을 통과한다면 `isAble`은 초기 상태인 `true`값을 가질 것이므로 `Yes`를 출력한다.

```java


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
```

### 3. DFS를 통한 모든 조합 검사 - 시간초과

총 인원수가 `n`명일 때, 길이 `n`의 배열을 만들어 모든 조합을 만든다. 각 조합으로 나뉘어진 두 팀에 대하여 아무도 시너지를 가지지 않으면 `isAble = true`로 설정하여 탐색을 멈추고 출력하며 끝까지 가능한 조합이 만들어지지 않으면 `No`를 출력하게하였다. 하지만 Brute Force답게 시간초과...

```java
  private static void dfs(int n) {
		if (isAble)
			return;
		
		if (n == K) {
			if (checkTeamAble()) {
				isAble = true;
			}
			return;
		}
		teamFlag[n] = false;
		dfs(n+1);
		teamFlag[n] = true;
		dfs(n+1);
	}

	private static boolean checkTeamAble() {
		HashSet<String>[] teams = new HashSet[2];
		teams[0] = new HashSet<>();
		teams[1] = new HashSet<>();
		
		for (int i = 0; i < K; i++) {
			if (teamFlag[i]) {
				if (teams[0].contains(players[i][1]) || teams[1].contains(players[i][0]))
					return false;

				teams[0].add(players[i][0]);
				teams[1].add(players[i][1]);
			} else {
				if (teams[0].contains(players[i][0]) || teams[1].contains(players[i][1]))
					return false;
				
				teams[0].add(players[i][1]);
				teams[1].add(players[i][0]);				
			}
		}
		for (int i = 0; i < K; i++) {
			if (teams[0].contains(players[i][0]) && teams[0].contains(players[i][1])) {
				return false;
			}
			else if (teams[1].contains(players[i][0]) && teams[1].contains(players[i][1])) {
				return false;
			}
		}
		
		return true;
	}
```
