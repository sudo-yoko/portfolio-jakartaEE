import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class DesktopClocker {

    static Point initialClick = null;
    static JLabel dateTimeLabel = null;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setUndecorated(true); // タイトルバーを非表示
        frame.setSize(360, 50);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // 中央に配置
        frame.setOpacity(0.9f);

        // パネルを作成
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1), // 境界線を黒に設定
                BorderFactory.createEmptyBorder(3, 3, 3, 3)// 内側の余白（上・左・下・右）
        ));
        frame.add(panel, BorderLayout.CENTER);

        // スクリーンショットボタンを作成
        JButton printButton = new JButton("0");
        printButton.setFont(new Font("Arial", Font.PLAIN, 20)); // モダンなフォントを設定
        printButton.setFocusable(false); // フォーカスをセットしない
        printButton.setToolTipText("スクリーンショットを撮る");
        printButton.addActionListener(e -> {
            try {
                Runtime.getRuntime().exec("explorer ms-screenclip:");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
                System.exit(1);
            }
        });
        panel.add(printButton);

        // 現在の日時を表示するラベル
        dateTimeLabel = new JLabel();
        dateTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dateTimeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        panel.add(dateTimeLabel);

        // 閉じるボタン
        JButton closeButton = new JButton("X");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 20));
        closeButton.setFocusable(false);
        closeButton.setToolTipText("閉じる");
        closeButton.addActionListener(e -> {
            frame.dispose(); // ウィンドウを閉じる
            System.exit(0); // プロセスを終了させる
        });
        panel.add(closeButton);

        // ウィンドウを移動できるようにする
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }
        });
        frame.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // フレームの現在の位置
                int thisX = frame.getLocation().x;
                int thisY = frame.getLocation().y;
                // ドラッグされた距離
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;
                // 新しい位置
                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                frame.setLocation(X, Y);
            }
        });

        // タイマーをセットして日時を毎秒更新
        new Timer(1000, (ActionEvent e) -> {
            updateDateTime();
        }).start();
        updateDateTime();

        frame.setVisible(true);
    }

    private static void updateDateTime() {
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        dateTimeLabel.setText(currentDateTime);
    }
}
