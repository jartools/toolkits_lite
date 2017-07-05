package com.bowlong.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import com.bowlong.lang.RndEx;
import com.bowlong.objpool.StringBufPool;
import com.bowlong.util.MapEx;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 生成图片字
 * 
 * @author canyon
 * 
 */
public class ImgEx extends ImageEx {
	static public int ImgW = 60;// 图片宽度(像素)
	static public int ImgH = 20;// 图片高度(像素)

	static public int num = 4; // 随机字数
	static public int FntHMin = 14;// 图片字最小高度(像素)
	static public int FntHMax = 18;// 图片字最大高度(像素)
	static private boolean isChange = false;

	// "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789";随机字库
	static public String strAll = "0123456789";

	static public final String MapKey_Code = "code";
	static public final String MapKey_Img = "img";

	static public final void defParameter() {
		if (!isChange)
			return;
		isChange = false;
		ImgW = 60;// 图片宽度(像素)
		ImgH = 20;// 图片高度(像素)
		num = 4; // 随机字数
		FntHMin = 14;// 图片字最小高度(像素)
		FntHMax = 18;// 图片字最大高度(像素)
	}

	/*** 取得图片字map对象[code:String,img:BufferedImage] **/
	static public final Map<String, Object> createRGB() {
		Map<String, Object> result = MapEx.newSortedMap();
		BufferedImage image = create(ImgW, ImgH);
		StringBuffer buff = StringBufPool.borrowObject();
		try {
			Graphics2D g2d = image.createGraphics();
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0, 0, ImgW, ImgH);
			g2d.setColor(Color.WHITE);
			g2d.fillRect(1, 1, ImgW - 2, ImgH - 2);

			for (int i = 1; i < ImgW - 1; ++i) {
				for (int j = 1; j < ImgH - 1; ++j) {
					if (RndEx.nextInt(10) < 2)
						continue;
					g2d.setColor(getRndColor(150, 240));
					g2d.drawLine(i, j, i, j);
				}
			}

			// int oldJiu = r.nextInt(HEIGHT - 6) + 3;
			// g2d.setColor(Color.RED);
			// for (int i = 1; i < WIDTH - 1; ++i) {
			// if (Math.random() < Math.random())
			// ++oldJiu;
			// else {
			// --oldJiu;
			// }
			//
			// if (oldJiu <= 1)
			// oldJiu = 2;
			// if (oldJiu > HEIGHT - 2) {
			// oldJiu = HEIGHT - 3;
			// }
			//
			// g2d.drawLine(i, oldJiu, i, oldJiu);
			// g2d.drawLine(i, oldJiu, i + 1, oldJiu);
			// }

			int cellX = ImgW / num - 1;

			for (int i = 0; i < num; ++i) {
				String tmp = RndEx.nextString(strAll);
				g2d.setColor(getRndColor(10, 100));
				int fontStyle = RndEx.nextInt(3);
				int fontSize = RndEx.nextInt(FntHMin, FntHMax);
				g2d.setFont(new Font("", fontStyle, fontSize));
				AffineTransform atf = new AffineTransform();
				atf.setToRotation(Math.random() / 8.0D - 0.081250000000000003D);
				g2d.setTransform(atf);
				g2d.drawString(tmp, i * cellX + 5, fontSize);
				buff.append(tmp);
			}
			g2d.dispose();
			result.put(MapKey_Code, buff.toString());
			result.put(MapKey_Img, image);
		} catch (Exception e) {
		} finally {
			StringBufPool.returnObject(buff);
		}
		return result;
	}

	static public final Map<String, Object> createRGBBy(int w, int h, int size) {
		if (h < 12 || size < 0 || w < (size - 10))
			return MapEx.newSortedMap();
		ImgH = h;
		ImgW = w;
		num = size;
		int cell = ImgW / num;
		FntHMax = cell + 3;
		FntHMin = cell - 1;
		isChange = true;
		return createRGB();
	}

	static public final Color getRndColor(int beg, int end) {
		int r = RndEx.nextInt(beg, end);
		int g = RndEx.nextInt(beg, end);
		int b = RndEx.nextInt(beg, end);
		return new Color(r, g, b);
	}

	/*** 输出图片 **/
	static public final String outImg(OutputStream outStream)
			throws IOException {
		defParameter();
		Map<String, Object> map = createRGB();
		if (MapEx.isEmpty(map))
			return "";
		BufferedImage img = (BufferedImage) map.get(MapKey_Img);
		ImageIO.write(img, JPEG_Min, outStream);
		return (String) map.get(MapKey_Code);
	}

	static public final String outImgBy(OutputStream outStream, int w, int h,
			int size) throws IOException {
		Map<String, Object> map = createRGBBy(w, h, size);
		if (MapEx.isEmpty(map))
			return "";
		BufferedImage img = (BufferedImage) map.get(MapKey_Img);
		ImageIO.write(img, JPEG_Min, outStream);
		return (String) map.get(MapKey_Code);
	}

	/*** 二维码图片 **/
	static public final BufferedImage createImg4QRCode(int w, int h,
			String content) {
		try {

			int BLACK = 0xFF000000;
			int WHITE = 0xFFFFFFFF;
			Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
			// 内容所使用编码
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 设置QR二维码的纠错级别——这里选择最高H级别
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
					BarcodeFormat.QR_CODE, w, h, hints);

			BufferedImage image = create(w, h);
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					image.setRGB(x, y, bitMatrix.get(x, y) ? BLACK : WHITE);
				}
			}
			image.flush();
			return image;
		} catch (Exception e) {
			return null;
		}
	}

	static public final void outImg4QRCode(OutputStream outStream,
			String content) throws IOException {
		outImg4QRCode(outStream, content, 300, 300);
	}

	static public final void outImg4QRCode(OutputStream outStream,
			String content, int w, int h) throws IOException {
		BufferedImage img = createImg4QRCode(w, h, content);
		if (img == null)
			return;
		ImageIO.write(img, JPEG_Min, outStream);
	}
}
