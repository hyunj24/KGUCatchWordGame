package main;

import javax.swing.*;
import game.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainMenu {
	Player p = null;
	PlayerRecord r = null;
	// 전체적인 메뉴를 구성하는 역할을 함
	MainFrame mainApp;

	public MainMenu(MainFrame app) {
		this.mainApp = app;
	}

	// 메인창
	public JPanel MainMenuPanel() {
		JPanel main = new JPanel();

		main.setLayout(new GridLayout(5, 1));

		// 사용자 정보 버튼 이미지 추가
		ImageIcon userInfoIcon = new ImageIcon("imgs/userInfo.jpg");
		JButton userInfoButton = new JButton();
		userInfoButton.setIcon(userInfoIcon);
		JLabel userInfoLabel = new JLabel("사용자 정보"); // 사용자 정보를 라벨로 추가
		userInfoLabel.setFont(new Font("돋움", Font.BOLD, 28));
		userInfoButton.setLayout(new BoxLayout(userInfoButton, BoxLayout.X_AXIS));
		userInfoButton.add(Box.createHorizontalGlue());
		userInfoButton.add(userInfoLabel);
		userInfoButton.add(Box.createHorizontalGlue());

		// 랭킹 버튼 이미지 추가
		ImageIcon rankingIcon = new ImageIcon("imgs/ranking.jpg");
		JButton rankingButton = new JButton();
		rankingButton.setIcon(rankingIcon);
		JLabel rankingLabel = new JLabel("랭킹");
		rankingLabel.setFont(new Font("돋움", Font.BOLD, 28));
		rankingButton.setLayout(new BoxLayout(rankingButton, BoxLayout.X_AXIS));
		rankingButton.add(Box.createHorizontalGlue());
		rankingButton.add(rankingLabel);
		rankingButton.add(Box.createHorizontalGlue());

		// 게임모드 버튼 이미지 추가
		ImageIcon gameModeIcon = new ImageIcon("imgs/gameWindow.jpg");
		JButton gameModeButton = new JButton();
		gameModeButton.setIcon(gameModeIcon);
		JLabel gameModeLabel = new JLabel("게임 START");
		gameModeLabel.setFont(new Font("돋움", Font.BOLD, 28));
		gameModeButton.setLayout(new BoxLayout(gameModeButton, BoxLayout.X_AXIS));
		gameModeButton.add(Box.createHorizontalGlue());
		gameModeButton.add(gameModeLabel);
		gameModeButton.add(Box.createHorizontalGlue());

		// 게임설명 버튼 이미지 추가
		ImageIcon explainGameIcon = new ImageIcon("imgs/explainGame.jpg");
		JButton explainGameButton = new JButton();
		explainGameButton.setIcon(explainGameIcon);
		JLabel explainGameLabel = new JLabel("게임설명");
		explainGameLabel.setFont(new Font("돋움", Font.BOLD, 28));
		explainGameButton.setLayout(new BoxLayout(explainGameButton, BoxLayout.X_AXIS));
		explainGameButton.add(Box.createHorizontalGlue());
		explainGameButton.add(explainGameLabel);
		explainGameButton.add(Box.createHorizontalGlue());

		// 게임종료 버튼 이미지 추가
		ImageIcon exitGameIcon = new ImageIcon("imgs/exitGame.jpg");
		JButton exitGameButton = new JButton();
		exitGameButton.setIcon(exitGameIcon);
		JLabel exitGameLabel = new JLabel("게임 종료");
		exitGameLabel.setFont(new Font("돋움", Font.BOLD, 28));
		exitGameButton.setLayout(new BoxLayout(exitGameButton, BoxLayout.X_AXIS));
		exitGameButton.add(Box.createHorizontalGlue());
		exitGameButton.add(exitGameLabel);
		exitGameButton.add(Box.createHorizontalGlue());

		userInfoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainApp.setContentPane(mainApp.getMenu().UserInfoPanel());
				mainApp.revalidate();
				mainApp.repaint();
			}
		});

		rankingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainApp.setContentPane(mainApp.getMenu().RankingPanel());
				mainApp.revalidate();
				mainApp.repaint();
			}
		});

		gameModeButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        // 버튼 색상 변경 (JOptionPane)
		        UIManager.put("Button.background", new Color(255, 255, 224)); // 버튼 배경 색상

		        String[] options = { "1단계", "2단계", "3단계", "4단계", "5단계" };
		        int level = JOptionPane.showOptionDialog(mainApp, "난이도를 선택하세요:", "난이도 선택", JOptionPane.DEFAULT_OPTION,
		                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		        // 게임 모드 버튼 누르면 캐치워드 게임 시작
		        if (level >= 0) {
		            Catchword catchwordGame = new Catchword(level, r);
		            mainApp.setContentPane(catchwordGame); // 메인메뉴 화면을 게임 화면으로 변경
		            mainApp.revalidate();
		            mainApp.repaint();
		            mainApp.setResizable(false);
		        }
		    }
		});


		explainGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainApp.setContentPane(mainApp.getMenu().ExplainGamePanel());
				mainApp.revalidate();
				mainApp.repaint();
			}
		});

		exitGameButton.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		        // 기존 Look and Feel을 저장
		        Color originalColor = UIManager.getColor("Button.background");

		        // 버튼의 배경색 변경
		        UIManager.put("Button.background", new Color(255, 255, 224));

		        // 확인 창 생성
		        int confirm = JOptionPane.showConfirmDialog(mainApp, 
		                "게임을 종료하시겠습니까?", 
		                "게임 종료", 
		                JOptionPane.YES_NO_OPTION);

		        // 선택된 옵션에 따라 동작
		        if (confirm == JOptionPane.YES_OPTION) {
		            GameManager GM = new GameManager();
		            GM.updateRecordTxt(); // 기록 갱신
		            System.exit(0); // 프로그램 종료
		        }

		        // 변경한 색상을 원래대로 복원
		        UIManager.put("Button.background", originalColor);
		    }
		});


		main.add(userInfoButton);
		main.add(rankingButton);
		main.add(gameModeButton);
		main.add(explainGameButton);
		main.add(exitGameButton);

		return main;
	}

	// 사용자 정보 창
	public JPanel UserInfoPanel() {
		p = mainApp.getCurrentPlayer();
		r = p.getRecord();

		String id = "ID : " + p.getId();
		String score;
		if (r != null)
			score = "Best Score : " + r.getBestScore() + "점 / " + r.getBestScoreLevel() + "레벨";
		else
			score = "Best Score : 기록이 없습니다.";

		// 패널 생성
		JPanel userInfoPanel = new JPanel() {
			protected void paintComponent(Graphics g) {
				Image background = new ImageIcon("imgs/Join.jpg").getImage();
				g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		userInfoPanel.setLayout(new BorderLayout(10, 10)); // 여백 추가

		// 상단 라벨 (제목)
		JLabel userInfoTitleLabel = new JLabel("사용자 정보", SwingConstants.CENTER);
		userInfoTitleLabel.setFont(new Font("Monospaced", Font.BOLD, 36)); // 큰 폰트
		userInfoTitleLabel.setForeground(Color.BLACK); // 텍스트 색상

		// 사용자 정보 내용을 HTML로 작성
		String userInfoHtml = "<html>"
				+ "<div style='text-align:center; font-size:24px; font-weight:bold; padding-bottom:10px;'>" + id
				+ "</div>" + "<div style='text-align:center; font-size:24px; font-weight:bold; padding-bottom:10px;'>"
				+ score + "</div>" + "</html>";

		JLabel userInfoContentLabel = new JLabel(userInfoHtml, SwingConstants.CENTER);
		userInfoContentLabel.setFont(new Font("Monospaced", Font.PLAIN, 20)); // 폰트 크기 조정
		userInfoContentLabel.setForeground(Color.BLACK); // 텍스트 색상
		userInfoContentLabel.setOpaque(false); // 배경 투명

		// 하단 버튼 (로그아웃 및 뒤로가기)
		JButton logoutButton = new JButton("로그아웃");
		logoutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainApp.currentPlayer = null;
				mainApp.setContentPane(mainApp.getMenu().MainMenuPanel());
				mainApp.revalidate();
				mainApp.repaint();
			}
		});

		JButton backButton = backButton();

		logoutButton.setBackground(new Color(255, 255, 224));
		backButton.setBackground(new Color(255, 255, 224));

		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		bottomPanel.setOpaque(false); // 배경 투명
		bottomPanel.add(logoutButton);
		bottomPanel.add(backButton);

		// 패널 구성
		userInfoPanel.add(userInfoTitleLabel, BorderLayout.NORTH); // 상단 제목
		userInfoPanel.add(userInfoContentLabel, BorderLayout.CENTER); // 중앙 사용자 정보
		userInfoPanel.add(bottomPanel, BorderLayout.SOUTH); // 하단 버튼

		return userInfoPanel;
	}

	// 랭킹 창
		public JPanel RankingPanel() {
			// 패널 생성
			JPanel rank = new JPanel() {
				protected void paintComponent(Graphics g) {
					Image background = new ImageIcon("imgs/RankingGround.jpg").getImage(); // 배경 이미지
					g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
					setOpaque(false);
					super.paintComponent(g);
				}
			};
			rank.setLayout(new BorderLayout(10, 10)); // 여백 추가

			// 상단 라벨 (제목)
			JLabel rankingLabel = new JLabel("사용자 랭킹", SwingConstants.CENTER);
			rankingLabel.setFont(new Font("Monospaced", Font.BOLD, 36)); // 큰 폰트
			rankingLabel.setForeground(Color.BLACK); // 텍스트 색상

			// 사용자 랭킹 정렬
			ArrayList<PlayerRecord> sortedRecords = new ArrayList<>(GameManager.recordList);

			// 점수와 레벨이 0인 기록을 제외
			sortedRecords.removeIf(record -> record.getBestScore() == 0 && record.getBestScoreLevel() == 0);

			// 점수와 레벨 기준으로 정렬
			sortedRecords.sort((r1, r2) -> {
				if (r2.getBestScore() != r1.getBestScore()) {
					return r2.getBestScore() - r1.getBestScore(); // 점수 높은 순으로
				} else {
					return r2.getBestScoreLevel() - r1.getBestScoreLevel(); // 동점이면 레벨 높은 순으로
				}
			});

			// 랭킹 내용을 HTML로 작성
			StringBuilder rankingHtml = new StringBuilder("<html>");
			rankingHtml.append("<div style='text-align:center;'>");
			rankingHtml.append(
					"<table style='border-collapse:collapse; margin:0; font-size:18px; border-spacing:0; padding:0;'>");

			// 테이블 헤더 추가
			rankingHtml.append("<tr style='font-weight:bold; background-color:#f2f2f2;'>");
			rankingHtml.append("<th style='padding:5px; border:1px solid black; margin:0; '>순위</th>");
			rankingHtml.append("<th style='padding:5px; border:1px solid black; margin:0; '>ID</th>");
			rankingHtml.append("<th style='padding:5px; border:1px solid black; margin:0; '>최고 점수</th>");
			rankingHtml.append("<th style='padding:5px; border:1px solid black; margin:0; '>레벨</th>");
			rankingHtml.append("</tr>");

			// 각 플레이어의 데이터를 행으로 추가
			int rankCounter = 1;
			for (PlayerRecord record : sortedRecords) {
				rankingHtml.append("<tr>");
				rankingHtml.append(String.format(
						"<td style='padding:5px; border:1px solid black; margin:0; text-align:center; '>%d</td>",
						rankCounter++));
				rankingHtml.append(String.format(
						"<td style='padding:5px; border:1px solid black; margin:0; text-align:center; '>%s</td>",
						record.getPlayerId()));
				rankingHtml.append(String.format(
						"<td style='padding:5px; border:1px solid black; margin:0; text-align:center; '>%d</td>",
						record.getBestScore()));
				rankingHtml.append(String.format(
						"<td style='padding:5px; border:1px solid black; margin:0; text-align:center; '>%d</td>",
						record.getBestScoreLevel()));
				rankingHtml.append("</tr>");
			}

			rankingHtml.append("</table>");
			rankingHtml.append("</div></html>");

			// 랭킹 내용을 포함한 라벨 생성
			JLabel rankingContentLabel = new JLabel(rankingHtml.toString(), SwingConstants.CENTER);
			rankingContentLabel.setFont(new Font("Monospaced", Font.PLAIN, 20)); // 폰트 크기 조정
			rankingContentLabel.setForeground(Color.BLACK); // 텍스트 색상
			rankingContentLabel.setOpaque(false); // 배경 투명

			// 랭킹 내용을 스크롤 가능하게 설정
			JScrollPane scrollPane = new JScrollPane(rankingContentLabel);
			scrollPane.setOpaque(false);
			scrollPane.getViewport().setOpaque(false); // 스크롤 영역 투명
			scrollPane.setBorder(BorderFactory.createEmptyBorder()); // 경계선 제거

			// 하단 버튼 (뒤로가기)
			JButton backButton = backButton();
			backButton.setBackground(new Color(255, 255, 224));

			JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
			bottomPanel.setOpaque(false); // 배경 투명
			bottomPanel.add(backButton);

			// 패널 구성
			rank.add(rankingLabel, BorderLayout.NORTH); // 상단 제목
			rank.add(scrollPane, BorderLayout.CENTER); // 중앙 랭킹 내용
			rank.add(bottomPanel, BorderLayout.SOUTH); // 하단 버튼

			return rank;
		}


	// 게임 설명 창
	public JPanel ExplainGamePanel() {
		JPanel explain = new JPanel() {
			protected void paintComponent(Graphics g) {
				Image background = new ImageIcon("imgs/ExplainGround.jpg").getImage();
				g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
				setOpaque(false);

				super.paintComponent(g);
			}
		};
		explain.setLayout(new BorderLayout());

		JLabel explainLabel = new JLabel("<html>게임 설명) 아래에 있는 버튼들을 눌러 목표 단어의 각 글자를 순서대로 맞추세요.<br>"
				+ "만약 올바른 글자를 누르면, 해당 글자는 파란색으로 바뀝니다. 이렇게 목표 단어의 모든 글자를 맞추면 그 라운드는 성공입니다!<br>"
				+ "만약 틀린 글자를 누르면, 배경이 짧게 빨간색으로 깜박이며, 새로운 목표 단어가 제시됩니다. 동시에 시간이 줄어들게 되니 주의하세요!<br><br>" + "<b>시간 관리:</b><br>"
				+ "각 라운드는 제한된 시간 내에 목표 단어를 맞춰야 합니다.<br>" + "목표 단어를 완성할 때마다 추가 시간이 주어지며 점수도 획득합니다.<br>"
				+ "틀릴 경우에는 벌칙으로 시간이 줄어들게 됩니다. 남은 시간 관리를 잘 해야 게임에서 승리할 수 있습니다!<br><br>" + "<b>라운드 진행:</b><br>"
				+ "단어를 맞추면 다음 라운드로 이동하며, 게임은 총 5라운드로 구성됩니다.<br>" + "각 라운드에서 단어를 맞출 때마다 새로운 글자가 섞여 나타납니다.<br>"
				+ "모든 라운드를 성공적으로 완료하거나 시간이 모두 소진될 때 게임이 종료됩니다.<br><br>" + "<b>아이템:</b><br>" 
				+ "1~3단계까지는 일시정지 아이템을 사용할 수 있습니다!<br>" + "그리고 4, 5단계에서는 일시정지 아이템 뿐만 아니라 '힌트' 아이템과 '시간추가' 아이템 중 하나를 사용할 수 있습니다.<br>"
				+ " '힌트' 를 사용할 경우 지금 눌러야 할 버튼에 빨간 테두리가 나오며, '시간 추가' 아이템은 10초가 추가됩니다.<br>" + "한 게임 당 둘 중 하나만 사용 가능하니 신중히 사용하세요!<br><br>"
				+ "<b>장애물:</b><br>" + "3단계부터는 장애물이 있습니다. 5초에 한 번씩 다음 두 가지 장애물이 등장합니다.<br>1) 암전 - 배경이 어두워지며 목표 단어가 가려집니다. 우상단의 '스위치' 버튼을 누르면 다시 밝아질 수 있어요!"
				+ "<br>2) 글자 가려짐 - 랜덤으로 세개의 버튼의 글자가 없어질 거예요. 그럴 땐 우상단의 연필 아이콘을 눌러보세요! 다시 글자가 등장합니다.<br><br>" 
				+ "<b>점수 계산 및 종료:</b><br>"
				+ "각 라운드를 완료하면 점수가 누적되고, 최종적으로 남은 시간을 기준으로 추가 점수가 계산됩니다.<br>"
				+ "<br>게임 종료 후 최종 점수와 함께 여러분의 최고 점수가 갱신됩니다. 친구들과 점수를 비교하며 최고 기록을 세워보세요!" + "</html>",
				SwingConstants.CENTER);


		explainLabel.setFont(new Font("돋음", Font.BOLD, 13));

		JButton backButton = backButton();
		backButton.setBackground(new Color(255, 255, 224));

		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		bottomPanel.setOpaque(false); // 배경 투명
		bottomPanel.add(backButton);

		explain.add(explainLabel, BorderLayout.CENTER);
		explain.add(bottomPanel, BorderLayout.SOUTH); // 하단 버튼

		return explain;
	}

	// 뒤로가기 버튼
	public JButton backButton() {
		JButton backButton = new JButton("뒤로가기");
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainApp.setContentPane(mainApp.getMenu().MainMenuPanel());
				mainApp.revalidate();
				mainApp.repaint();
			}
		});
		return backButton;
	}

	// 게임 종료 창
	public JPanel ExitGamePanel() {
		JPanel exit = new JPanel();
		exit.setLayout(new BorderLayout());

		JLabel explainLabel = new JLabel("설명", SwingConstants.CENTER);

		exit.add(explainLabel, BorderLayout.CENTER);

		return exit;
	}

}