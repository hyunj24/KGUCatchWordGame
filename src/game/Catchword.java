package game;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import game.Button;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import main.*;

public class Catchword extends JPanel implements ActionListener {
	Image gameBackGround = new ImageIcon("imgs/gameWindow.jpg").getImage();
	ImageIcon lockIcon = new ImageIcon(
			new ImageIcon("imgs/lock.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
	ImageIcon pencilIcon = new ImageIcon(
			new ImageIcon("imgs/pencil.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
	ImageIcon swichIcon = new ImageIcon(
			new ImageIcon("imgs/swich.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
	Random random = new Random();
	// 파일에서 단어 로드
	private JButton penButton;
	private JButton swichButton;
	private Set<JButton> lockedButtons = new HashSet<>();
	private ArrayList<String> words = loadWordsFromFile("words.txt");
	private int currentWordIndex;
	private String targetWord;
	private JLabel timerLabel;
	private JLabel stageLabel;
	private JLabel problemLabel;
	private JPanel targetPanel;
	private ArrayList<JLabel> targetLabels;
	private JPanel topPanel;
	private JPanel gridPanel;
	private JProgressBar timeBar;
	private JPanel darkOverlay;

	private JButton[][] buttons;
	private int currentIndex = 0;
	private int time = 30;
	private int plusTime = 0;
	private int Psize;
	private Timer timer;
	private int score = 0;
	private int totalScore = 0;
	private static int MAX_ROUNDS = 5;
	private int roundsCompleted = 0;
	private int finalScore = 0;
	private int minusTime = 1;
	PlayerRecord r = null;
	private int selectedLevel = 0;

	private boolean isPaused = false;
	private boolean hasPaused = false;
	private JButton pauseButton;

	private JButton hintButton;
	private boolean hintUsed = false; // 힌트 사용 여부
	private int hintPenalty = 5;

	private JButton addTimeButton;

	private boolean itemUsedThisRound = false;

	private JLabel keyLabel;
	private Timer lightTimer;

	private static final String[] EXTRA_CHARS = { "가", "나", "다", "라", "마", "바", "사", "아", "자", "차", "카", "타", "파", "하",
			"강", "난", "당", "락", "만", "방", "산", "알", "장", "착", "칼", "탕", "팔", "한", "병", "리", "남", "한", "채", "도", "유",
			"시", "계", "풀", "물", "고", "노", "또", "로", "모", "보", "소", "오", "조", "초", "코", "토", "포", "호"};

	private static Difficulty[] difficulties = { new Difficulty(60, 3, 5, 1, 0, 3), new Difficulty(50, 4, 5, 2, 0, 3),
			new Difficulty(40, 5, 5, 3, 3, 3), new Difficulty(30, 6, 5, 4, 4, 4), new Difficulty(30, 7, 5, 5, 5, 4) };

	private ArrayList<String> loadWordsWithLength(int length) {
		ArrayList<String> filteredWords = new ArrayList<>();
		for (String word : words) {
			if (word.length() == length) {
				filteredWords.add(word);
			}
		}
		return filteredWords;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(gameBackGround, 0, 0, getWidth(), getHeight(), this);

		if (isPaused) {
			g.setColor(new Color(0, 0, 0, 150)); // 반투명 검은색
			g.fillRect(0, 0, getWidth(), getHeight());
		} else {
			repaint();
		}
	}

	public Catchword(int difficultyLevel, PlayerRecord r) {
		this.selectedLevel = difficultyLevel;
		this.r = r;
		setLayout(null);
		setSize(1280, 720);
		int width = getWidth();
		int height = getHeight();

		Difficulty selectedDifficulty = difficulties[difficultyLevel];

		time = 60;

		MAX_ROUNDS = selectedDifficulty.numRounds;
		score = selectedDifficulty.score;
		plusTime = selectedDifficulty.plustime;
		Psize = selectedDifficulty.Psize;
		words = loadWordsWithLength(selectedDifficulty.wordLength);

		currentWordIndex = random.nextInt(words.size());
		targetWord = words.get(currentWordIndex);

		JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		infoPanel.setBackground(new Color(255, 0, 0, 0));
		stageLabel = new JLabel("단계: " + score);
		stageLabel.setBackground(new Color(255, 0, 0, 0));
		problemLabel = new JLabel("문제: 1 / " + MAX_ROUNDS);
		problemLabel.setBackground(new Color(255, 0, 0, 0));

		stageLabel.setFont(new Font("돋움", Font.BOLD, 24));
		problemLabel.setFont(new Font("돋움", Font.BOLD, 24));
		infoPanel.add(stageLabel);
		infoPanel.add(problemLabel);

		topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(new Color(255, 0, 0, 0));
		topPanel.add(infoPanel, BorderLayout.NORTH);

		targetPanel = new JPanel();
		targetPanel.setBackground(new Color(255, 0, 0, 0));

		targetLabels = new ArrayList<>();
		for (char c : targetWord.toCharArray()) {
			JLabel letterLabel = new JLabel(String.valueOf(c));
			letterLabel.setFont(new Font("돋움", Font.BOLD, 40));
			targetPanel.add(letterLabel);
			targetLabels.add(letterLabel);
		}
		topPanel.add(targetPanel, BorderLayout.CENTER);
		
		//일시정지 버튼
		ImageIcon pauseIcon = new ImageIcon("imgs/pause.png");
		pauseButton = new JButton();
		pauseButton.setIcon(pauseIcon);
		pauseButton.setOpaque(false); 
		pauseButton.setBorder(null);
		pauseButton.setBackground(new Color(255,0,0,0));

		//힌트 버튼
		ImageIcon hintIcon = new ImageIcon("imgs/hint.png");
		hintButton = new JButton();
		hintButton.setIcon(hintIcon);
		hintButton.setOpaque(false); 
		hintButton.setBorder(null);
		hintButton.setBackground(new Color(255,0,0,0));
		//시간추가 버튼
		ImageIcon addTimeIcon = new ImageIcon("imgs/addTime.png");
		addTimeButton = new JButton();
		addTimeButton.setIcon(addTimeIcon);
		addTimeButton.setOpaque(false); 
		addTimeButton.setBorder(null);
		addTimeButton.setBackground(new Color(255,0,0,0));
		
		hintButton.setEnabled(selectedLevel >= 3 && !hintUsed);
		addTimeButton.setEnabled(selectedLevel >= 3 && !hintUsed);

		Dimension buttonSize = new Dimension(90, 40);
		pauseButton.setPreferredSize(buttonSize);
		hintButton.setPreferredSize(buttonSize);
		addTimeButton.setPreferredSize(buttonSize);

		pauseButton.addActionListener(e -> {
			if (isPaused) {
				resumeGame();
			} else {
				togglePause();
			}

		});

		hintButton.addActionListener(e -> {
			if (!itemUsedThisRound) {
				useHint(hintButton);
				itemUsedThisRound = true;
				hintButton.setEnabled(false);
				addTimeButton.setEnabled(false); // 다른 아이템도 비활성화
			}
		});

		addTimeButton.addActionListener(e -> {
			if (!itemUsedThisRound) {
				int addTime = 10;
				addTime(addTime);

				totalScore -= hintPenalty;
				if (totalScore < 0)
					totalScore = 0;
				itemUsedThisRound = true;
				hintButton.setEnabled(false); // 다른 아이템도 비활성화
				addTimeButton.setEnabled(false);
			}
		});

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		buttonPanel.setBackground(new Color(255, 0, 0, 0));
		buttonPanel.add(pauseButton);
		buttonPanel.add(hintButton);
		buttonPanel.add(addTimeButton);
		
		topPanel.add(buttonPanel, BorderLayout.SOUTH);

		topPanel.setSize(width, height / 5);
		topPanel.setLocation(0, height / 10);
		add(topPanel);

		buttons = new JButton[Psize][Psize];
		JPanel canvas = new JPanel(null);
		canvas.setBackground(new Color(255, 0, 0, 0));
		gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(Psize, Psize, 10, 10));
		gridPanel.setBackground(new Color(255, 0, 0, 0));
		gridPanel.setSize(getWidth() / 2, getHeight() / 2);
		gridPanel.setLocation(getWidth() / 2 - gridPanel.getWidth() / 2,
				getHeight() / 2 - gridPanel.getHeight() / 2 + 50);

		add(gridPanel);

		for (int i = 0; i < Psize; i++) {
			for (int j = 0; j < Psize; j++) {
				buttons[i][j] = new JButton();
				buttons[i][j].setFont(new Font("돋움", Font.BOLD, 40));
				buttons[i][j].addActionListener(this);
				buttons[i][j].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				gridPanel.add(buttons[i][j]);
				buttons[i][j].setUI(new Button());
			}
		}
		penButton = new JButton(pencilIcon); // 버튼에 연필 이미지 추가
		penButton.setBounds(getWidth() - 100, 20, 50, 50); // 우측 상단 위치
		penButton.setBorder(BorderFactory.createEmptyBorder()); // 버튼 테두리 제거
		penButton.setContentAreaFilled(false); // 배경 제거
		penButton.addActionListener(e -> unlockAllButtons()); // 클릭 시 동작 정의

		add(penButton); // 패널에 추가

		swichButton = new JButton(swichIcon); // 버튼에 스위치 이미지 추가
		swichButton.setBounds(getWidth() - 100, 80, 50, 50); // 우측 상단 위치
		swichButton.setBorder(BorderFactory.createEmptyBorder()); // 버튼 테두리 제거
		swichButton.setContentAreaFilled(false); // 배경 제거
		swichButton.addActionListener(e -> {
		    turnOnLights(); // 화면 밝게 설정
		});


		add(swichButton); // 패널에 추가
		
		startRandomFeatureTimer();
		timeBar();
		shuffleButtons();
		startTimer();
		//RandomEraser();
		createDarkOverlay();
		//startLightTimer();

	}
	private void startRandomFeatureTimer() {
		if (selectedLevel < 2) {
	        return; // 기능은 3, 4, 5 단계에서만 실행
	    }
	    Timer randomFeatureTimer = new Timer(8000, new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            // 0 또는 1 중 하나를 랜덤으로 선택
	            int randomFeature = random.nextInt(2);

	            if (randomFeature == 0) {
	                // 불 끄기 실행
	                turnOffLights();
	            } else {
	                // 잠금 기능 실행
	                RandomEraser();
	            }
	        }
	    });
	    randomFeatureTimer.start();
	}
	private void startLightTimer() {
	    lightTimer = new Timer(5000, new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            turnOffLights(); // 5초마다 화면 어둡게
	        }
	    });
	    lightTimer.start();
	}
	private void RandomEraser() {
	    if (selectedLevel < 2) {
	        return; // 기능은 3, 4, 5 단계에서만 실행
	    }

	    ArrayList<JButton> availableButtons = new ArrayList<>();
	    for (int i = 0; i < Psize; i++) {
	        for (int j = 0; j < Psize; j++) {
	            if (buttons[i][j].isEnabled()) {
	                availableButtons.add(buttons[i][j]);
	            }
	        }
	    }

	    if (availableButtons.size() < 3) {
	        // 남아있는 버튼이 3개 미만인 경우 처리하지 않음
	        return;
	    }

	    // 버튼들을 무작위로 섞고, 상위 3개의 버튼을 잠금 처리
	    Collections.shuffle(availableButtons);
	    for (int k = 0; k < 3; k++) {
	        JButton button = availableButtons.get(k);

	        // 원래 텍스트 저장
	        String originalText = button.getText();
	        button.putClientProperty("originalText", originalText);

	        // 잠금 상태로 변경
	        button.setText(""); // 텍스트 제거
	        button.setIcon(lockIcon);
	        button.setEnabled(false); // 버튼 비활성화
	    }

	    revalidate();
	    repaint();
	}

	 private JButton switchButton;

    
     
  private void createDarkOverlay() {
         darkOverlay = new JPanel() {
             @Override
             protected void paintComponent(Graphics g) {
                 super.paintComponent(g);
                 g.setColor(new Color(0, 0, 0, 150)); // 반투명 검은색
                 g.fillRect(0, 0, getWidth(), getHeight());
             }
         };
         darkOverlay.setBounds(0, 0, getWidth(), getHeight());
         darkOverlay.setVisible(false); // 처음역 비활성화
         darkOverlay.setOpaque(false);
         add(darkOverlay);
     }

     // 화면 어둡게 설정
     private void turnOffLights() {
         darkOverlay.setVisible(true);
         // 모든 텍스트 보이지 않도록 보용
         for (JLabel label : targetLabels) {
             label.setForeground(new Color(0, 0, 0, 0));
         }
         for (int i = 0; i < Psize; i++) {
             for (int j = 0; j < Psize; j++) {
                 buttons[i][j].setEnabled(false);
             }
         }
     }

     // 화면 밝게 설정
     private void turnOnLights() {
         darkOverlay.setVisible(false);
         for (JLabel label : targetLabels) {
             label.setForeground(Color.BLACK); // 발게 설정시 무조건 표시
         }
         for (int i = 0; i < targetLabels.size(); i++) {
             JLabel label = targetLabels.get(i);
             if (i < currentIndex) {
                 label.setForeground(Color.BLUE); // 이미 맞춘 글자는 파란색 유지
             } else {
                 label.setForeground(Color.BLACK); // 나머지는 검은색
             }
         }
         for (int i = 0; i < Psize; i++) {
             for (int j = 0; j < Psize; j++) {
                 buttons[i][j].setEnabled(true);
             }
         }
     }
	 private void unlockAllButtons() {
		    for (int i = 0; i < Psize; i++) {
		        for (int j = 0; j < Psize; j++) {
		            JButton button = buttons[i][j];
		            if (button.getIcon() == lockIcon) {
		                // 잠금 해제: 원래 텍스트 복원 및 아이콘 제거
		                button.setText((String) button.getClientProperty("originalText"));
		                button.setIcon(null);
		                button.setEnabled(true);
		            }
		        }
		    }
		    revalidate();
		    repaint();
		}
	private void timeBar() {
		int margin = 20; // 화면의 좌우 여백
		int barWidth = getWidth() - (2 * margin); // 화면 너비에서 여백을 뺀 너비
		int barHeight = 30; // 바의 높이
		int barX = margin; // 왼쪽 여백
		int barY = getHeight() - 100; // 바의 세로 위치
		UIManager.put("ProgressBar.selectionForeground", Color.BLACK); // 진행 바 텍스트 색상
		UIManager.put("ProgressBar.selectionBackground", Color.BLACK);
		timeBar = new JProgressBar(0, time); // 초기 범위 설정 (0 ~ 최대 시간)
		timeBar.setValue(time); // 초기값 설정
		timeBar.setForeground(Color.green); // 진행 색상
		timeBar.setStringPainted(true); // 텍스트 표시 활성화
		timeBar.setFont(new Font("돋움", Font.BOLD, 18));
		timeBar.setBackground(Color.DARK_GRAY);
		timeBar.setBounds(barX, barY, barWidth, barHeight); // 위치 및 크기 설정
		timeBar.setString("남은 시간: " + time + "초"); // 텍스트로 시간 표시

		add(timeBar); // 패널에 추가
	}

	// 게임 일시정지 기능 메서드
	private void togglePause() {
		if (hasPaused)
			return;

		isPaused = !isPaused;
		if (isPaused) {
			timer.stop();
			ImageIcon returnIcon = new ImageIcon("imgs/return.png");
			pauseButton.setIcon(returnIcon);
			pauseButton.setOpaque(false); 
			pauseButton.setBorder(null);

			JLabel pauseMessage = new JLabel("일시정지 중");
			pauseMessage.setFont(new Font("돋움", Font.BOLD, 40));
			pauseMessage.setForeground(Color.RED);
			targetPanel.add(pauseMessage);

			for (JLabel label : targetLabels) {
				label.setText("");
			}
			disableButton();
			hasPaused = true;
		} else {
			resumeGame();
		}
	}

	// 게임 다시 시작 메서드
	private void resumeGame() {
		hasPaused = false;
		isPaused = false;
		enableButton();
		pauseButton.setEnabled(false);
		//pauseButton.setText("일시정지");

		currentWordIndex = random.nextInt(words.size());
		targetWord = words.get(currentWordIndex);
		resetGame();
		timer.start();
		repaint();
	}

	// 버튼 비활성화 메서드
	private void disableButton() {
		for (int i = 0; i < Psize; i++) {
			for (int j = 0; j < Psize; j++) {
				buttons[i][j].setEnabled(false);
			}
		}
	}

	// 버튼 활성화 메서드
	private void enableButton() {
		for (int i = 0; i < Psize; i++) {
			for (int j = 0; j < Psize; j++) {
				buttons[i][j].setEnabled(true);
			}
		}

	}

	private void useHint(JButton hintButton) {
		// 힌트 사용 처리
		hintUsed = true;
		hintButton.setEnabled(false); // 버튼 비활성화

		// 현재 맞춰야 할 글자를 찾음
		if (currentIndex < targetWord.length()) {
			char hintChar = targetWord.charAt(currentIndex); // 현재 맞춰야 할 글자

			for (int i = 0; i < Psize; i++) {
				for (int j = 0; j < Psize; j++) {
					JButton button = buttons[i][j];
					if (button.getText().equals(String.valueOf(hintChar))) {
						// 글자가 일치하는 버튼에 깜박임 효과 추가
						startBlinkingEffect(button);
						break; // 해당 글자만 깜박이므로 루프 종료
					}
				}
			}
		}

		// 패널티 적용
		totalScore -= hintPenalty;
		if (totalScore < 0)
			totalScore = 0; // 점수 음수 방지
	}

	private void startBlinkingEffect(JButton button) {
		final Border originalBorder = button.getBorder(); // 원래 테두리 저장
		final Border highlightBorder = BorderFactory.createLineBorder(Color.RED, 7); // 빨간색 강조 테두리

		Timer blinkTimer = new Timer(200, new ActionListener() {
			private boolean isBlinking = false; // 깜박임 상태를 추적
			private int blinkCount = 0; // 깜박임 횟수

			@Override
			public void actionPerformed(ActionEvent e) {
				if (blinkCount >= 8) { // 총 8번 깜박임
					button.setBorder(originalBorder); // 원래 테두리로 복원
					((Timer) e.getSource()).stop(); // 타이머 중지
					return;
				}

				// 테두리 전환
				button.setBorder(isBlinking ? originalBorder : highlightBorder);
				isBlinking = !isBlinking; // 상태 반전
				blinkCount++;
			}
		});

		blinkTimer.start(); // 타이머 시작
	}

	private void resetItemUsage() {
		itemUsedThisRound = false;
		hintButton.setEnabled(!itemUsedThisRound && selectedLevel >= 3 && !hintUsed);
		addTimeButton.setEnabled(!itemUsedThisRound && selectedLevel >= 3);
	}

	// 타이머 설정 메서드
	private void startTimer() {
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				time--;
				timeBar.setValue(time);
				timeBar.setString("남은 시간: " + time + "초"); // 시간 텍스트 업데이트

				// 시간에 따른 색상 변화
				if (time <= 10) {
					timeBar.setForeground(Color.RED);
				} else if (time <= 20) {
					timeBar.setForeground(Color.ORANGE);
				} else {
					timeBar.setForeground(Color.GREEN);
				}

				if (time == 0) {
					timer.stop();
					JOptionPane.showMessageDialog(Catchword.this, "시간 초과! 게임이 종료되었습니다.");
					showFinalScore(totalScore);
				}
			}
		});
		timer.start();
	}

	private void addTime(int plusTime) {
		time += plusTime;
		if (time > timeBar.getMaximum()) {
			time = timeBar.getMaximum();
		}
		timeBar.setValue(time);

		// 시간 변경 내용을 표시
		timeBar.setString("남은 시간: " + time + "초 + " + plusTime + "초");

		// 2초 후에 원래의 시간 표시로 돌아옴
		Timer displayTimer = new Timer(2000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timeBar.setString("남은 시간: " + time + "초");
				((Timer) e.getSource()).stop();
			}
		});
		displayTimer.setRepeats(false);
		displayTimer.start();
	}

	private void penaltyTime(int minusTime) {
		time -= minusTime;
		if (time < 0) {
			time = 0;
		}
		timeBar.setValue(time);

		// 시간 변경 내용을 표시
		timeBar.setString("남은 시간: " + time + "초 - " + minusTime + "초");

		// 2초 후에 원래의 시간 표시로 돌아옴
		Timer displayTimer = new Timer(2000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timeBar.setString("남은 시간: " + time + "초");
				((Timer) e.getSource()).stop();
			}
		});
		displayTimer.setRepeats(false);
		displayTimer.start();

	}

	private void resetGame() {
		currentIndex = 0;
		currentWordIndex = random.nextInt(words.size());
		targetWord = words.get(currentWordIndex);

		targetPanel.removeAll(); // 기존의 JLabel 제거
		targetLabels.clear(); // 새로운 단어를 넣기위해 리스트 초기화

		for (char c : targetWord.toCharArray()) {
			JLabel letterLabel = new JLabel(String.valueOf(c));
			letterLabel.setFont(new Font("돋움", Font.BOLD, 40));
			letterLabel.setForeground(Color.BLACK);
			letterLabel.setBackground(new Color(255, 0, 0, 0));
			targetPanel.add(letterLabel);
			targetLabels.add(letterLabel);
		}

		// targetPanel을 다시 그려 화면에 반영(없어도 되지만 없으면 오류날수있음)
		targetPanel.revalidate();
		targetPanel.repaint();
		this.repaint();

		updateProblemLabel(); // 각 라운드에서 문제 번호 업데이트
		shuffleButtons();
	}

	private void shuffleButtons() {
		ArrayList<Character> chars = new ArrayList<>();

		for (char c : targetWord.toCharArray()) {
			chars.add(c);
		}

		while (chars.size() < Psize * Psize) {
			char extraChar = generateRandomExtraChar();
			if (!chars.contains(extraChar)) {
				chars.add(extraChar);
			}
		}

		Collections.shuffle(chars);
		int index = 0;
		for (int i = 0; i < Psize; i++) {
			for (int j = 0; j < Psize; j++) {
				buttons[i][j].setText(chars.get(index).toString());
				index++;
			}
		}

	}

	private char generateRandomExtraChar() {
		Random random = new Random();
		return EXTRA_CHARS[random.nextInt(EXTRA_CHARS.length)].charAt(0);
	}

	private ArrayList<String> loadWordsFromFile(String filename) {
		ArrayList<String> wordList = new ArrayList<>();
		Scanner filein = openFile(filename);

		while (filein.hasNext()) {
			wordList.add(filein.next());
		}

		filein.close();
		return wordList;
	}

	private Scanner openFile(String filename) {
		Scanner filein = null;
		try {
			filein = new Scanner(new File(filename));
		} catch (Exception e) {
			System.out.printf("파일 오픈 실패: %s\n", filename);
			throw new RuntimeException(e);
		}
		return filein;
	}

	private void updateTargetLabel() {
		for (int i = 0; i < targetLabels.size(); i++) {
			if (i < currentIndex) {
				targetLabels.get(i).setForeground(Color.BLUE); // 맞춘 글자를 파란색으로 변경
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton clickedButton = (JButton) e.getSource();
		String clickedText = clickedButton.getText();

		// 목표 단어의 현재 인덱스 글자와 일치하는지 확인
		if (currentIndex < targetWord.length() && clickedText.charAt(0) == targetWord.charAt(currentIndex)) {
			// 맞은 경우: 파란색 테두리로 설정

			currentIndex++;
			updateTargetLabel();

			if (currentIndex == targetWord.length()) {
				addTime(plusTime);
				totalScore += score;
				roundsCompleted++;
				shuffleButtons();

				if (roundsCompleted == MAX_ROUNDS) {
					timer.stop();
					// JOptionPane.showMessageDialog(this, "성공! 모든 단어를 맞췄습니다.");
					showFinalScore(totalScore);
					return;
				}

				currentIndex = 0;
				resetGame();
			}
		} else {
			// 틀린 경우: 빨간색 테두리로 설정

			penaltyTime(minusTime);

			JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
			if (mainFrame != null) {
				JLayeredPane layeredPane = mainFrame.getLayeredPane();
				JPanel redOverlay = new JPanel() {
					@Override
					protected void paintComponent(Graphics g) {
						super.paintComponent(g);
						g.setColor(new Color(255, 0, 0, 100)); // 반투명 빨간색, 투명도 100
						g.fillRect(0, 0, getWidth(), getHeight());
					}
				};
				redOverlay.setOpaque(false); // 패널 자체는 불투명하지 않게 설정
				redOverlay.setBounds(0, 0, mainFrame.getWidth(), mainFrame.getHeight());
				layeredPane.add(redOverlay, JLayeredPane.PALETTE_LAYER);

				Timer flashTimer = new Timer(500, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent evt) {
						layeredPane.remove(redOverlay);
						layeredPane.repaint();
					}
				});
				flashTimer.setRepeats(false);
				flashTimer.start();
			}

			Toolkit.getDefaultToolkit().beep();

			currentIndex = 0;
			resetGame();
		}

		// 모든 버튼의 상태를 초기화하여 틀린 상태에서만 빨간색 테두리를 유지
		// shuffleButtons();
	}

	private void showFinalScore(int totalScore) {
		// 난이도별 1문제당 점수 설정 (5단계부터 1단계까지 점수 차이)
		int[] difficultyScores = { 1, 2, 3, 4, 5 }; // 1단계 1점, 2단계 2점, ... 5단계 5점

		// 선택한 난이도에 맞는 점수 가져오기
		int difficultyScore = difficultyScores[selectedLevel]; // selectedLevel은 0부터 시작

		// 라운드 점수 (1문제당 점수 * 라운드 수)
		int roundScore = roundsCompleted * difficultyScore;

		// 남은 시간 점수 계산 (남은 시간 1초당 1점)
		int timeScore = time; // 남은 시간이 1초당 1점씩 추가됨

		// r에서 원래 최고기록 점수와 레벨
		int bestScore = r.getBestScore();
		int bestLevel = r.getBestScoreLevel();

		// 최종 점수 계산
		finalScore = roundScore + timeScore;

		// 플레이어 기록 업데이트
		if (finalScore > bestScore) { //점수가 올랐을 때
			if ((selectedLevel + 1) >= bestLevel) { //레벨도 기존보다 높을 때
				r.updateBestScoreAndLevel(finalScore, selectedLevel + 1); //점수, 레벨 모두 업데이트
			}
			else { 
				if ((selectedLevel + 1) == bestLevel) // 점수는 올랐으나 레벨은 그대로일 때
					r.updateBestScore(finalScore); // 점수만 업데이트
				else r.updateBestScoreAndLevel(finalScore, (selectedLevel + 1)); // 점수는 올랐으나 레벨은 낮은 레벨일 때, 해당 점수를 딴 레벨을 넣어줘야 하므로 둘다 업뎃
			}
		} else if ((finalScore == bestScore) && (selectedLevel + 1) > bestLevel) { //점수는 같은데 레벨만 올랐을 때
			r.updateBestScoreLevel(selectedLevel + 1); //레벨만 업데이트
		}

		ScorePanel scorePanel = new ScorePanel(time, finalScore, selectedLevel + 1, roundsCompleted, bestScore,
				bestLevel);
		JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		if (mainFrame instanceof MainFrame) {
			((MainFrame) mainFrame).setContentPane(scorePanel);
			mainFrame.revalidate();
			mainFrame.repaint();
		}
	}

	private void updateProblemLabel() {
		problemLabel.setText("문제: " + (roundsCompleted + 1) + " / " + MAX_ROUNDS);
	}
}

/*
 * public static void main(String[] args) { Catchword game = new Catchword(1);
 * game.setVisible(true); }
 * 
 * }
 */
