package com.doofus.market.marketdataparserservice;

import com.doofus.market.BseDataParser;
import com.doofus.market.DataParser;
import com.doofus.market.model.BseInputRecord;
import com.doofus.market.model.BseOutputRecord;
import com.doofus.market.utils.ParserUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
public class FileUploadController {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);

  @PostMapping("/")
  public @ResponseBody byte[] handleFileUpload(
      @RequestParam("file") MultipartFile file, @RequestParam("filename") String fileName) throws IOException {

    // TODO Get zip, extract

    DataParser<BseInputRecord, BseOutputRecord> bseDataParser = new BseDataParser();
    final List<BseInputRecord> bseInputRecords;
    bseInputRecords = bseDataParser.read(file.getInputStream());

    final List<BseOutputRecord> bseOutputRecords = bseDataParser.convert(bseInputRecords);
    bseOutputRecords.forEach(
        bseOutputRecord -> bseOutputRecord.setDate(ParserUtils.getDateForRecords(fileName)));

    return bseDataParser.write(bseOutputRecords).getBytes();
  }

}
