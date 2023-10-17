import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

class Block {
    private int index;
    private long timestamp;
    private String data;
    private String previousHash;
    private String hash;

    public Block(int index, long timestamp, String data, String previousHash) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        String dataToHash = index + timestamp + data + previousHash;
        StringBuilder hexString = new StringBuilder();

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = messageDigest.digest(dataToHash.getBytes());

            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hexString.toString();
    }

    public int getIndex() {
        return index;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getData() {
        return data;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }
}

public class Blockchain
{
    private List<Block> chain;

    public Blockchain() {
        this.chain = new ArrayList<>();
        createGenesisBlock();
    }

    private void createGenesisBlock() {
        Block genesisBlock = new Block(0, System.currentTimeMillis(), "Genesis Block", "0");
        chain.add(genesisBlock);
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public void addBlock(Block newBlock) {
        newBlock = new Block(chain.size(), System.currentTimeMillis(), newBlock.getData(), getLatestBlock().getHash());
        chain.add(newBlock);
    }

    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                return false;
            }

            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();

        Block block1 = new Block(1, System.currentTimeMillis(), "Data 1", blockchain.getLatestBlock().getHash());
        blockchain.addBlock(block1);

        Block block2 = new Block(2, System.currentTimeMillis(), "Data 2", blockchain.getLatestBlock().getHash());
        blockchain.addBlock(block2);

        Block block3 = new Block(3, System.currentTimeMillis(), "Data 3", blockchain.getLatestBlock().getHash());
        blockchain.addBlock(block3);

        // Print the blocks in the blockchain
        for (Block block : blockchain.chain) {
            System.out.println("Block Index: " + block.getIndex());
            System.out.println("Block Hash: " + block.getHash());
            System.out.println("Previous Block Hash: " + block.getPreviousHash());
            System.out.println("Block Data: " + block.getData());
            System.out.println();
        }

        System.out.println("Blockchain is valid: " + blockchain.isChainValid());
}
}