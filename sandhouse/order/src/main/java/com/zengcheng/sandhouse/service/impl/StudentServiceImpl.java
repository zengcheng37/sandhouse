package com.zengcheng.sandhouse.service.impl;

import com.zengcheng.sandhouse.common.entity.Student;
import com.zengcheng.sandhouse.common.mapper.StudentMapper;
import com.zengcheng.sandhouse.service.StudentService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * InnoDB free: 11264 kB 服务实现类
 * </p>
 *
 * @author zengcheng
 * @since 2019-04-11
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

}
