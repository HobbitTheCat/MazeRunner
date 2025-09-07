import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Output {
    private final String scorePath = "Score.csv";
    private final static String animationPath = "Animation.txt";
    private String outputPath = "Output.txt";
    private Game game;

    private void setGame(Game game) {
        this.game = game;
    }

    private void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    private Game getGame() {
        return this.game;
    }

    private String getOutputPath() {
        return this.outputPath;
    }

    private String getScorePath() {
        return this.scorePath;
    }

    public Output(Game game) {
        this.setGame(game);
        this.setOutputPath(this.getGame().getPath());
    }

    public void outputMap(boolean seeAll) {
        if (!seeAll) this.output(toFinalString(generateArray(false)));
        else this.output(toFinalString(generateArray(true)));
    }

    public void outputMapToConsole(boolean seeAll) {
        if (!seeAll) this.outputConsole(toFinalString(generateArray(false)));
        else this.outputConsole(toFinalString(generateArray(true)));
    }

    public String[][] generateArray(boolean seeAll) {
        Map map = this.getGame().getMap();

        String[][] outputString = new String[map.getHeight() * 4 + 1][map.getWidth()];
        for (int row = 0; row < map.getHeight(); row++) {
            for (int layer = 0; layer < 5; layer++) {
                for (int column = 0; column < map.getWidth(); column++) {
                    String lay;
                    if (!seeAll) lay = map.getCell(row, column).getLayers()[layer];
                    else lay = map.getCell(row, column).getLayers(true)[layer];

                    if (lay != null) outputString[row * 4 + layer][column] = lay;
                }
            }
        }

        return outputString;
    }

    private String toFinalString(String[][] outputString) {
        StringBuilder finalOutput = new StringBuilder();
        for (String[] strings : outputString) {
            for (String string : strings) {
                finalOutput.append(string);
            }
            finalOutput.append("\n");
        }
        return finalOutput.toString();
    }

    public void outputConsole(String outputString) {
        System.out.println(outputString);
    }

    public void output(String outputString) {
        try {
            File file = new File(this.getOutputPath());
            FileWriter fw = new FileWriter(file, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(outputString);
            bw.close();
        } catch (IOException e) {
            System.out.println("Error of output");
        }
    }

    public void saveScore(int minute, int seconds) {
        String size = this.getGame().getMap().getWidth() + " column by " + this.getGame().getMap().getHeight() + " row";
        String csvRow = String.format("%s,%d,%d,%s", this.getGame().getPlayer().getName(), minute, seconds, size);

        ArrayList<String> dataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(this.getScorePath()))) {
            String line;
            while ((line = br.readLine()) != null) dataList.add(line);
        } catch (IOException e) {
            System.out.println("Error of reading");
        }

        boolean status = false;
        int count = 0;
        while (!status && count < dataList.size()) {
            String[] values = dataList.get(count).split(",");
            int minuteRead = Integer.parseInt(values[1]);
            int secondsRead = Integer.parseInt(values[2]);

            if (minute > minuteRead) {
                count += 1;
            } else if (seconds > secondsRead) {
                count += 1;
            } else {
                status = true;
                dataList.add(count, csvRow);
            }
        }

        if (count >= dataList.size()) dataList.add(count, csvRow);

        try (FileWriter writer = new FileWriter(this.getScorePath(), false)) {
            for (String line : dataList) writer.write(line + "\n");
        } catch (IOException e) {
            System.out.println("Error of output");
        }
    }
    public String getPreviousScore() {
        StringBuilder sb = new StringBuilder();
        sb.append("Leader board:\n");
        try (BufferedReader br = new BufferedReader(new FileReader(this.getScorePath()))) {
            String line;
            int count = 1;
            while ((line = br.readLine()) != null) {
                String[] elements = line.split(",");
                if (elements.length > 0) {
                    sb.append(count).append(") ").append("Name: ").append(elements[0]).append(" Time: ").append(elements[1]).append(" min ").append(elements[2]).append(" sec ");
                    sb.append("Map: ").append(elements[3]).append("\n");
                }
                count += 1;
            }
        } catch (IOException e) {
            System.out.println("Error of reading");
        }
        return (sb.toString());
    }

    // Not part of the project
    private String[][][] readAnimation(int index) {
        String line = "";
        try {
            Path path = Paths.get(Output.animationPath);
            line = Files.readAllLines(path).get(index);
        } catch (IOException e) {
            System.out.println("Error of reading");
        }
        String[] divLine = line.split("/");

        String[][] divDivLine = new String[divLine.length][];
        for (int count = 0; count < divLine.length; count++) {
            divDivLine[count] = divLine[count].split(",");
        }

        String[][][] resultLine = new String[divDivLine.length][divDivLine[0].length][1];
        for (int i = 0; i != divDivLine.length; i++) {
            for (int j = 0; j != divDivLine[i].length; j++) {
                resultLine[i][j] = new String[]{divDivLine[i][j].replace("|", "")};
            }
        }
        return resultLine;
    }

    private String[][] extract(String[][] array, int topLeftRow, int topLeftColumn) {
        Output.toOtherForm(array);

        topLeftRow *= 4;
        int bottomRightRow = topLeftRow + 4;
        topLeftColumn *= 8;
        int bottomRightColumn = topLeftColumn + 8;

        int numRows = bottomRightRow - topLeftRow + 1;
        int numColumns = bottomRightColumn - topLeftColumn + 1;
        String[][] extractedArray = new String[numRows][numColumns];
        for (int i = 0; i < numRows; i++) {
            System.arraycopy(array[topLeftRow + i], topLeftColumn, extractedArray[i], 0, numColumns);
        }
        return extractedArray;
    }

    private void replace(String[][] initial, String[][] replacement, int topLeftRow, int topLeftColumn) {
        Output.toOtherForm(initial);
        Output.toOtherForm(replacement);
        if (topLeftRow >= 0 && topLeftColumn >= 0) {
            if (initial.length > replacement.length + topLeftRow - 1 && initial[topLeftRow].length > topLeftColumn + replacement[0].length - 1) {
                for (int repRow = 0; repRow < replacement.length; repRow++) {
                    System.arraycopy(replacement[repRow], 0, initial[repRow + topLeftRow], topLeftColumn, replacement[repRow].length);
                }
            }
        }
    }

    private void replaceCorners(String[][] extractedCell, String[][] beforeCell, String[][] afterCell) {
        for (int i = 0; i < extractedCell.length; i++) {
            for (int j = 0; j < extractedCell[i].length; j++) {
                switch (extractedCell[i][j]) {
                    case "@":
                        extractedCell[i][j] = afterCell[i][j];
                        break;
                    case "#":
                        extractedCell[i][j] = beforeCell[i][j];
                        break;
                }
            }
        }
    }

    private static String[][] copyArray(String[][] initialArr) {
        int rows = initialArr.length;
        String[][] copyArray = new String[rows][];
        for (int i = 0; i < rows; i++) {
            copyArray[i] = new String[initialArr[i].length];
            System.arraycopy(initialArr[i], 0, copyArray[i], 0, initialArr[i].length);
        }
        return copyArray;
    }

    private static void toOtherForm(String[][] initialArr) {
        for (int i = 0; i < initialArr.length; i++) {
            ArrayList<String> resArray = new ArrayList<>();
            for (String str : initialArr[i]) {
                char[] chars = str.toCharArray();
                for (char ch : chars) resArray.add(String.valueOf(ch));
            }
            initialArr[i] = resArray.toArray(new String[0]);
        }
    }

    public void animationBreak(String direction, Cell currentCell) {
        Cell targetCell = currentCell.getCellByDirection(direction);
        String inverseDirection = switch (direction) {
            case "top" -> "bottom";
            case "bottom" -> "top";
            case "left" -> "right";
            case "right" -> "left";
            default -> "";
        };
        if(targetCell.isVisibleWithWallOn(inverseDirection)){
           this.animationBreakWall(direction, currentCell);
        }else this.animationBreakDiscover(inverseDirection, targetCell);


//        Cell targetCell = currentCell.getCellByDirection(direction);
//            switch (direction) {
//                case "top": animationBreakDiscover("bottom", targetCell);break;
//                case "bottom": animationBreakDiscover("top", targetCell);break;
//                case "left": animationBreakDiscover("right", targetCell);break;
//                case "right": animationBreakDiscover("left", targetCell);break;
//                default: System.out.println("Unknown direction");break;
//            }
    }

    private void animationBreakDiscover(String direction, Cell currentCell) {
        if(currentCell.repairWall(direction).equals("success")) {
            String[][] initialArr = this.generateArray(false);
            String[][] extractedCell = this.extract(initialArr, currentCell.getRow(), currentCell.getColumn());
            String[][] backupCell = this.extract(initialArr, currentCell.getRow(), currentCell.getColumn());
            if (currentCell.breakWall(direction).equals("success")) {
                String[][][] animationDs, animationBr, animationTW; //Discover, Break, Wall
                int columnForWall, animLen;
                switch (direction) {
                    case "left":
                        animationDs = this.readAnimation(0);
                        animationBr = this.readAnimation(1);
                        animationTW = this.readAnimation(2);
                        animLen = animationDs.length;
                        columnForWall = 0;
                        break;
                    case "right":
                        animationDs = this.readAnimation(3);
                        animationBr = this.readAnimation(1);
                        animationTW = this.readAnimation(4);
                        animLen = animationDs.length;
                        columnForWall = extractedCell[0].length - 1;
                        break;
                    case "bottom":
                        animationDs = this.readAnimation(6);
                        animationBr = this.readAnimation(5);
                        animationTW = animationBr;
                        columnForWall = extractedCell.length - 1;
                        animLen = animationDs.length;
                        break;
                    case "top":
                        animationDs = this.readAnimation(7);
                        animationBr = this.readAnimation(5);
                        animationTW = animationBr;
                        columnForWall = 0;
                        animLen = animationDs.length;
                        break;
                    default:
                        throw new RuntimeException("Unknown direction");
                }
                for (int frame = 0; frame < animLen; frame++) {
                    switch (direction) {
                        case "left", "right":
                            this.replace(extractedCell, animationTW[frame], 0, 0);
                            this.replace(extractedCell, animationTW[frame], 4, 0);
                            this.replace(extractedCell, animationDs[frame], 1, 0);
                            this.replace(extractedCell, animationBr[frame], 1, columnForWall);
                            break;
                        case "bottom":
                            this.replace(extractedCell, animationDs[frame], 0, 0);
                            this.replace(extractedCell, animationBr[frame], columnForWall, 0);
                            break;
                        case "top":
                            this.replace(extractedCell, animationDs[frame], 1, 0);
                            this.replace(extractedCell, animationBr[frame], columnForWall, 0);
                            break;
                    }
                    this.replaceCorners(extractedCell, Output.copyArray(backupCell), this.extract(this.generateArray(false), currentCell.getRow(), currentCell.getColumn()));
                    this.replace(initialArr, extractedCell, currentCell.getRow() * 4, currentCell.getColumn() * 8);
                    this.output(toFinalString(initialArr));
                    try {
                        TimeUnit.MILLISECONDS.sleep(120);
                    } catch (InterruptedException ignore) {
                    }
                    extractedCell = Output.copyArray(backupCell);
                }
            }
        }
    }

    private void animationBreakWall(String direction, Cell currentCell) {
        if (currentCell.repairWall(direction).equals("success")) {
            String[][] initialArr = this.generateArray(false);
            String[][] extractedCell = this.extract(initialArr, currentCell.getRow(), currentCell.getColumn());
            String[][] backupCell = Output.copyArray(extractedCell);
            if (currentCell.breakWall(direction).equals("success")) {
                String[][][] animationBr; //Break
                int animLen;
                switch (direction) {
                    case "top", "bottom":
                        animationBr = this.readAnimation(5);
                        animLen = animationBr.length;
                        break;
                    case "left", "right":
                        animationBr = this.readAnimation(1);
                        animLen = animationBr.length;
                        break;
                    default:
                        throw new RuntimeException("Unknown direction");
                }
                for (int frame = 0; frame < animLen; frame++) {
                    switch (direction) {
                        case "top":
                            this.replace(extractedCell, animationBr[frame], 0, 0);
                            break;
                        case "bottom":
                            this.replace(extractedCell, animationBr[frame], extractedCell.length - 1, 0);
                            break;
                        case "left":
                            this.replace(extractedCell, animationBr[frame], 1, 0);
                            break;
                        case "right":
                            this.replace(extractedCell, animationBr[frame], 1, extractedCell[0].length - 1);
                            break;
                    }
                    this.replaceCorners(extractedCell, Output.copyArray(backupCell), this.extract(this.generateArray(false), currentCell.getRow(), currentCell.getColumn()));
                    this.replace(initialArr, extractedCell, currentCell.getRow() * 4, currentCell.getColumn() * 8);
                    this.output(toFinalString(initialArr));
                    try {
                        TimeUnit.MILLISECONDS.sleep(120);
                    } catch (InterruptedException ignore) {
                    }
                    extractedCell = Output.copyArray(backupCell);
                }
            }
        }
    }

    public void animationKab00m(Cell targetCell){
        if (targetCell != null && targetCell.isVisible()) {
            String[][] initialArr = this.generateArray(false);
            String[][] extractedCell = this.extract(initialArr, targetCell.getRow(), targetCell.getColumn());
            String[][] backupCell = Output.copyArray(extractedCell);
            String[][][] animationKb = this.readAnimation(8);
            for (String[][] strings : animationKb) {
                this.replace(extractedCell, strings, 1, 1);
                this.replaceCorners(extractedCell, Output.copyArray(backupCell), this.extract(this.generateArray(false), targetCell.getRow(), targetCell.getColumn()));
                this.replace(initialArr, extractedCell, targetCell.getRow() * 4, targetCell.getColumn() * 8);
                this.output(toFinalString(initialArr));
                try {
                    TimeUnit.MILLISECONDS.sleep(120);
                } catch (InterruptedException ignore) {
                }
                extractedCell = Output.copyArray(backupCell);
            }
        }
    }
    public void animationKab00m(String direction, Cell currentCell){
        Cell targetCell = currentCell.getCellByDirection(direction);
        this.animationKab00m(targetCell);
    }
}
