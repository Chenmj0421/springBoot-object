package com.rs.oss.admin;

import com.reger.dubbo.annotation.Inject;
import com.rs.common.base.BaseResult;
import com.rs.common.base.BaseResultConstant;
import com.rs.common.util.RequestUtil;
import com.rs.common.util.StringUtil;
import com.rs.common.util.httpclient.HttpUtil;
import com.rs.common.util.uuid.UuId;
import com.rs.oss.dao.model.OssConfig;
import com.rs.oss.dao.model.OssFile;
import com.rs.oss.dao.model.OssServer;
import com.rs.oss.dao.model.OssServerExample;
import com.rs.oss.rpc.api.OssApiService;
import com.rs.oss.rpc.api.OssFileService;
import com.rs.oss.rpc.api.OssServerService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UploadController {
    private final static Logger log = LoggerFactory.getLogger(UploadController.class);

//    /**
//     * 集群名称
//     */
//    static String ClusterName = "nsstargate";
//    /**
//     * url
//     */
//    private static final String HADOOP_URL = "hdfs://" + ClusterName;
//    /**
//     * 配置
//     */
//    public static Configuration conf;
    @Inject
    OssApiService ossApiService;
    @Inject
    OssServerService ossServerService;
    @Inject
    OssFileService ossFileService;

    @ApiOperation(value = "文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "folder", value = "文件夹，如果传请传医院名称（英文或者拼音", required = false, dataType = "String")
    })
    @PostMapping("/upload")
    @ResponseBody
    public Object upload(@RequestParam(value = "files", required = true) MultipartFile[] files, HttpServletRequest request, ModelMap model) throws IOException {
        Map<String, Object> result = new HashMap<>(16);
        OssConfig ossConfig = ossApiService.selectOssConfig();
        Long maxSize = 0L;
        if (ossConfig == null) {
            return BaseResult.newFailure(BaseResultConstant.FAILED.getCode(), "oss 配置为空");
        }
        String userId = request.getParameter("userId");
        String folder = request.getParameter("folder");
        if (null == userId) {
            return BaseResult.newFailure(BaseResultConstant.FAILED.getCode(), "用户id为空");
        }
        maxSize = ossConfig.getMaxSize() == null ? 0L : ossConfig.getMaxSize();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                // 保存文件
                try {
                    byte[] bytes = file.getBytes();
                    long count = bytes.length;
                    String originalFilename = file.getOriginalFilename();
                    String originalName = StringUtil.getFileName(originalFilename);
                    String fileType = StringUtil.getFileExt(originalFilename);
                    if (!maxSize.equals(0L)) {
                        if (count > maxSize) {
                            return BaseResult.newFailure(BaseResultConstant.FAILED.getCode(), "文件大小超过限制");
                        }
                    }
                    // 重新生成文件名，防止文件名重复
                    String fileName = UuId.getUUID();
                    OssServerExample ossServerExample = new OssServerExample();
                    ossServerExample.createCriteria().andConfigIdEqualTo(ossConfig.getId());
                    List<OssServer> ossServerList = ossServerService.selectByExample(ossServerExample);
                    if (ossServerList.size() == 0) {
                        return BaseResult.newFailure(BaseResultConstant.FAILED.getCode(), "当前集群下无hadoop server配置！");
                    }
//                    this.setCon(ossServerList);
//                    if (isHa()) {
//                        // 暂时不对高ha的情况进行处理,后续再扩展
//                    }
                    // 不是高ha的情况下
                    String ip = ossServerList.get(0).getIp();
                    String uploadPort = ossServerList.get(0).getFilePort().toString();
                    String path = "http://".concat(ip).concat(":").concat(uploadPort).concat("/webhdfs/v1/");
                    if (null != folder) {
                        path = path.concat(folder).concat("/");
                    }
                    path = path.concat(fileName).concat(".").concat(fileType);
                    String url = path.concat("?op=CREATE&user.name=").concat(ossConfig.getUserName().concat("&overwrite=false"));
                    boolean isTrue = HttpUtil.uploadFileToHadoop(url, file, ip, fileName.concat(".").concat(fileType));
                    if (isTrue) {
                        OssFile ossFile = new OssFile();
                        ossFile.setAddress(path.concat("?op=OPEN&user.name=").concat(ossConfig.getUserName()));
                        ossFile.setOriginalName(originalName);
                        ossFile.setUserId(userId);
                        ossFile.setIp(ip);
                        if (folder != null) {
                            ossFile.setRemoteFilePath(folder);
                        }
                        String id = ossFileService.save(ossFile);
                        result.put("filePath", RequestUtil.getBasePath(request).concat("/").concat(id));
                    }
                    result.put("success", true);
                    result.put("error", "文件上传成功！");
                    return result;

                } catch (IOException e) {
                    result.put("success", false);
                    result.put("error", e.getMessage());
                    return result;
                }

            }

        }
        result.put("success", false);
        result.put("error", "文件为空");
        return result;
    }

    @ApiOperation(value = "获取文件的访问地址")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "folder", value = "文件夹，如果传请传医院名称（英文或者拼音", required = false, dataType = "String")
    })
    @GetMapping("/getFileUrl/{id}")
    public String getFileUrl(@PathVariable("id") String id) throws IOException {
        OssFile ossFile = ossFileService.selectByPrimaryKey(id);
        String address = null;
        if (ossFile != null) {
            address = ossFile.getAddress();
        }
        if (address != null) {
            String url = HttpUtil.getFilePathFromHadoop(address, ossFile.getIp());
            return "redirect:" + url;
        }
        return null;
    }

//    /**
//     * 初始化配置
//     *
//     * @param ossServerList
//     * @return
//     */
//    private void setCon(List<OssServer> ossServerList) {
//        conf = new Configuration();
//        conf.set("fs.defaultFS", HADOOP_URL);
//        conf.set("dfs.nameservices", ClusterName);
//        conf.set("dfs.ha.namenodes." + ClusterName, "nn1");
//        for (int i = 0; i < ossServerList.size(); i++) {
//            String prefix = ".nn".concat(String.valueOf(i));
//            String host = ossServerList.get(i).getIp().concat(":").concat(ossServerList.get(i).getPort().toString());
//            conf.set("dfs.namenode.rpc-address." + ClusterName + prefix, host);
//
//        }
//        //conf.setBoolean(name, value);
//        conf.set("dfs.client.failover.proxy.provider." + ClusterName,
//                "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
//        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
//    }
//
//    /**
//     * 是否是高ha
//     *
//     * @return true 是高ha，比如多nameNode ，false 不是高ha，比如单机或者主从
//     * @throws IOException
//     */
//    private boolean isHa() throws IOException {
//        FileSystem fs = FileSystem.get(conf);
//        DistributedFileSystem hdfs = (DistributedFileSystem) fs;
//        return HAUtil.isHAEnabled(conf);
//    }

}
