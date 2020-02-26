import java.io.File;

public class SizeCalculatingRunnable implements Runnable {
    private String path;
    private boolean recursive;
    private boolean stopRequested = false;
    private long fileSize;

    SizeCalculatingRunnable(String path, boolean recursive) {
        this.path = path;
        this.recursive = recursive;
    }

    @Override
    public void run() {
        calculateDirectorySize(new File(path).listFiles(), recursive);
    }

    public long getFileSize() {
        return fileSize;
    }

    private void calculateDirectorySize(File[] files, boolean recursive) {
        if (stopRequested == true) {
            return;
        }
        if (files == null)
            return;
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("Directory: " + file.getName());
                if (recursive == true) {
                    calculateDirectorySize(file.listFiles(), true); // Calls same method again.
                }
            } else {
                System.out.println("File: " + file.getName());
                fileSize += file.length();
            }
        }
    }
    public void requestStop(){
        stopRequested = true;
    }
}
