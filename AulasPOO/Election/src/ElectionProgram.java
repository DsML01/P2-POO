import java.util.Scanner;

public class ElectionProgram
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of voters: ");
        int totalVoters = sc.nextInt();

        int votesPSDB = 0;
        int votesPMDB = 0;
        int nullVotes = 0;

        for(int i = 0; i < totalVoters; i++)
        {
            System.out.println("Enter vote (45 for PSDB, 15 for PMDB):");
            int vote = sc.nextInt();

            if(vote == 45) votesPSDB++;
            else if(vote == 15) votesPMDB++;
            else nullVotes++;
        }

        int validVotes = votesPSDB + votesPMDB;

        System.out.println("Votes for PSDB (45): " + votesPSDB);
        System.out.println("Votes for PMDB (15): " + votesPMDB);
        System.out.println("Null Votes : " + nullVotes);

        int totalVotes = validVotes + nullVotes;
        double pmdbPercentage = ((double) votesPMDB / totalVotes) * 100.0;
        double psdbPercentage = ((double) votesPSDB / totalVotes) * 100.0;

        if(nullVotes > validVotes/2) System.out.println("Election has been cancelled.");

        else if(votesPSDB > votesPMDB) System.out.println("Winner: PSDB (45) with " + psdbPercentage + "% of PSDB");

        else if(votesPMDB > votesPSDB) System.out.println("Winner: PMDB (15) with " + pmdbPercentage + "% of PMDB");

        else System.out.println("It's a tie. No winner!");
    }
}
