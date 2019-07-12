package com.zengcheng.sandhouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zengcheng.sandhouse.common.entity.StudentEntity;
import com.zengcheng.sandhouse.common.mapper.StudentMapper;
import com.zengcheng.sandhouse.service.IStudentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zengcheng
 * @since 2019-07-12
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, StudentEntity> implements IStudentService {

}
