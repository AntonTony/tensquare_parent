package com.Tony.notice.controller;

import com.Tony.notice.pojo.Notice;
import com.Tony.notice.pojo.NoticeFresh;
import com.Tony.notice.service.NoticeService;
import com.baomidou.mybatisplus.plugins.Page;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.apache.ibatis.annotations.Delete;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author AntonTony
 * @version 1.0
 * @GitHub https://github.com/AntonTony
 */
@RestController
@RequestMapping("/notice")
//解决跨域问题
@CrossOrigin
public class NoticeController {

    @Autowired
    private NoticeService noticeService ;

    /**
     * 根据条件分页查询
     * POST
     * http://127.0.0.1:9014/notice/search/{page}/{size}
     * @param page
     * @param size
     * @param notice
     * @return
     */
    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.POST)
    public Result search(@PathVariable Integer page,
                         @PathVariable Integer size,
                         @RequestBody Notice notice){
        //使用 Mybatis Plus 提供的Page对象
        Page<Notice> pageData = noticeService.selectByPage(page,size,notice);

        //使用分页结果集显示查询数据
        PageResult<Notice> pageResult = new PageResult<>(pageData.getTotal(),pageData.getRecords());
        return new Result(true,StatusCode.OK,"条件分页查询成功",pageResult);
    }

    /**
     * 根据id查询消息通知
     * http://127.0.0.1:9014/notice/{noticeId}
     * GET
     * @param noticeId
     * @return
     */
    @GetMapping("{noticeId}")
    public Result noticeById(@PathVariable String noticeId){
       Notice notice =  noticeService.selectById(noticeId);
        return new Result(true, StatusCode.OK,"根据Id查询消息成功",notice);
    }

    /**
     * 新增通知
     * http://127.0.0.1:9014/notice
     * POST
     * @param notice
     * @return
     */
    @PostMapping
    public Result noticeSave(@RequestBody Notice notice){
        noticeService.save(notice);
        return new Result(true,StatusCode.OK,"成功新增通知");
    }



    /**
     * 修改通知
     * http://127.0.0.1:9014/notice
     * PUT
     * @param notice
     * @return
     */
    @PutMapping
    public Result udpateById(@RequestBody Notice notice){
        noticeService.updateById(notice);
        return new Result(true,StatusCode.OK,"修改消息成功");
    }


    /**
     * 根据用户id查询该用户的待推送消息（新消息）
     *  GET
     *  http://127.0.0.1/notice/fresh/{userId}/{page}/{size}
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "fresh/{userId}/{page}/{size}")
    public Result freshPage(@PathVariable String userId,
                            @PathVariable Integer page,
                            @PathVariable Integer size){
        Page<NoticeFresh> pageData = noticeService.freshPage(userId,page,size);

        PageResult<NoticeFresh> pageResult = new PageResult<>(
                pageData.getTotal(),pageData.getRecords()
        );

        return new Result(true,StatusCode.OK,"根据ID查询成功",pageResult);
    }

    /**
     * 根据条件删除待推送消息（新消息）
     * DELETE
     * http://127.0.0.1/notice/fresh/{noticeFresh}
     * @param noticeFresh
     * @return
     */
    @DeleteMapping("fresh")
    public Result freshDelete(@RequestBody NoticeFresh noticeFresh){
        noticeService.freshDelete(noticeFresh);
        return new Result(true,StatusCode.OK,"删除成功");
    }



}
