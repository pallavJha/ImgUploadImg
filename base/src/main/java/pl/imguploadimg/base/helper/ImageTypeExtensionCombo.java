package pl.imguploadimg.base.helper;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import pl.imguploadimg.base.log.LoggerService;

@Component("imageTypeExtensionCombo")
public class ImageTypeExtensionCombo {

	@Autowired
	@Qualifier("loggerService")
	LoggerService loggerService;
	
	public static Map<String, String> typeExtMap = new HashMap<String, String>();
	
	@PostConstruct
	public void insertion(){
		loggerService.spacedLog("Initiating ImageTypeExtensionCombo#insertion()!");
		typeExtMap.put("image/x-jg",".art");
		typeExtMap.put("image/bmp",".art");
		typeExtMap.put("image/bmp",".bmp");
		typeExtMap.put("image/x-windows-bmp",".bmp");
		typeExtMap.put("image/vnd.dwg",".dwg");
		typeExtMap.put("image/x-dwg",".dwg");
		typeExtMap.put("image/vnd.dwg",".dxf");
		typeExtMap.put("image/x-dwg",".dxf");
		typeExtMap.put("image/fif",".fif");
		typeExtMap.put("image/florian",".flo");
		typeExtMap.put("image/vnd.fpx",".fpx");
		typeExtMap.put("image/vnd.net-fpx",".fpx");
		typeExtMap.put("image/g3fax",".fpx");
		typeExtMap.put("image/gif",".gif");
		typeExtMap.put("image/x-icon",".ico");
		typeExtMap.put("image/ief",".ief");
		typeExtMap.put("image/ief",".ief");
		typeExtMap.put("image/jpeg",".ief");
		typeExtMap.put("image/pjpeg",".ief");
		typeExtMap.put("image/jpeg",".ief");
		typeExtMap.put("image/jpeg",".jpe");
		typeExtMap.put("image/pjpeg",".jpe");
		typeExtMap.put("image/jpeg",".jpe");
		typeExtMap.put("image/pjpeg",".jpe");
		typeExtMap.put("image/jpeg",".jpg");
		typeExtMap.put("image/pjpeg",".jpg");
		typeExtMap.put("image/x-jps",".jps");
		typeExtMap.put("image/jutvision",".jut");
		typeExtMap.put("image/vasa",".mcf");
		typeExtMap.put("image/naplps",".nap");
		typeExtMap.put("image/naplps",".nap");
		typeExtMap.put("image/x-niff",".nif");
		typeExtMap.put("image/x-niff",".nif");
		typeExtMap.put("image/x-portable-bitmap",".pbm");
		typeExtMap.put("image/x-pict",".pct");
		typeExtMap.put("image/x-pcx",".pcx");
		typeExtMap.put("image/x-portable-graymap",".pgm");
		typeExtMap.put("image/x-portable-greymap",".pgm");
		typeExtMap.put("image/pict",".pic");
		typeExtMap.put("image/pict",".pic");
		typeExtMap.put("image/x-xpixmap",".pic");
		typeExtMap.put("image/png",".png");
		typeExtMap.put("image/x-portable-anymap",".pnm");
		typeExtMap.put("image/x-portable-pixmap",".ppm");
		typeExtMap.put("image/x-quicktime",".qif");
		typeExtMap.put("image/x-quicktime",".qti");
		typeExtMap.put("image/x-quicktime",".qti");
		typeExtMap.put("image/cmu-raster",".ras");
		typeExtMap.put("image/x-cmu-raster",".ras");
		typeExtMap.put("image/cmu-raster",".ras");
		typeExtMap.put("image/vnd.rn-realflash",".ras");
		typeExtMap.put("image/x-rgb",".rgb");
		typeExtMap.put("image/vnd.rn-realpix",".rgb");
		typeExtMap.put("image/vnd.dwg",".svf");
		typeExtMap.put("image/x-dwg",".svf");
		typeExtMap.put("image/tiff",".tif");
		typeExtMap.put("image/x-tiff",".tif");
		typeExtMap.put("image/tiff",".tif");
		typeExtMap.put("image/x-tiff",".tif");
		typeExtMap.put("image/florian",".tif");
		typeExtMap.put("image/vnd.wap.wbmp",".tif");
		typeExtMap.put("image/x-xbitmap",".xbm");
		typeExtMap.put("image/x-xbm",".xbm");
		typeExtMap.put("image/xbm",".xbm");
		typeExtMap.put("image/vnd.xiff",".xif");
		typeExtMap.put("image/x-xpixmap",".xpm");
		typeExtMap.put("image/xpm",".xpm");
		typeExtMap.put("image/png",".xpm");
		typeExtMap.put("image/x-xwd",".xwd");
		typeExtMap.put("image/x-xwindowdump",".xwd");
	}
}
