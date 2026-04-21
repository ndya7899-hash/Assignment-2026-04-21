import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class EdgeDetectionApp {
    public static void main(String[] args) {
        try {
            // 1. 讀取原始照片 (請確保專案資料夾裡有這張圖)
            BufferedImage src = ImageIO.read(new File("input.jpg"));
            int w = src.getWidth();
            int h = src.getHeight();

            // 準備兩張空的圖：一張存灰階，一張存邊緣結果
            double[][] gray = new double[h][w];
            BufferedImage outputX = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

            // 2. 轉成灰階數字陣列
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int rgb = src.getRGB(x, y);
                    int r = (rgb >> 16) & 0xff;
                    int g = (rgb >> 8) & 0xff;
                    int b = rgb & 0xff;
                    gray[y][x] = (r + g + b) / 3.0; 
                }
            }

            // 3. 計算邊緣 (套用講義公式：Ix = [f(x+1, y) - f(x-1, y)] / 2)
            for (int y = 1; y < h - 1; y++) {
                for (int x = 1; x < w - 1; x++) {
                    double ix = (gray[y][x + 1] - gray[y][x - 1]) / 2.0;

                    // 4. 數值轉換：因為 ix 可能是負的，我們要把它平移到 128 (灰色)
                    // 沒變化是灰色(128)，變化大變白(>128)或變黑(<128)
                    int pixelVal = (int)(ix + 128);
                    
                    // 限制範圍在 0~255 之間防止出錯
                    pixelVal = Math.max(0, Math.min(255, pixelVal));

                    // 組合成 RGB 存入新圖片
                    int rgb = (pixelVal << 16) | (pixelVal << 8) | pixelVal;
                    outputX.setRGB(x, y, rgb);
                }
            }

            // 存檔
            ImageIO.write(outputX, "jpg", new File("edge_x.jpg"));
            System.out.println("邊緣圖生成成功！請查看 edge_x.jpg");

        } catch (Exception e) {
            System.out.println("發生錯誤：" + e.getMessage());
        }
    }
}