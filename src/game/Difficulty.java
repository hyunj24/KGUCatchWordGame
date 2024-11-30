package game;


class Difficulty {
    int wordLength; // 목표 단어 길이
    int numRounds; // 라운드 수
    int score; //문제당 점수
    int plustime; //추가할 시간
    int Psize; //패널 크기

    public Difficulty(int wordLength, int numRounds, int score,  int plustime, int Psize) {
        this.wordLength = wordLength;
        this.numRounds = numRounds;
        this.score = score;
        this.plustime = plustime;
        this.Psize = Psize;
    } 
}
