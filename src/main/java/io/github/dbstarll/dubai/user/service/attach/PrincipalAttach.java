package io.github.dbstarll.dubai.user.service.attach;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.DeleteResult;
import io.github.dbstarll.dubai.model.entity.Entity;
import io.github.dbstarll.dubai.model.service.Implementation;
import io.github.dbstarll.dubai.model.service.Service;
import io.github.dbstarll.dubai.model.service.attach.CoreAttachs;
import io.github.dbstarll.dubai.user.entity.join.PrincipalBase;
import io.github.dbstarll.dubai.user.service.impl.PrincipalAttachImplemental;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Map.Entry;

@Implementation(PrincipalAttachImplemental.class)
public interface PrincipalAttach<E extends Entity & PrincipalBase> extends CoreAttachs {
    /**
     * 生成基于主体ID的过滤条件.
     *
     * @param principalId 主体ID
     * @return 基于主体ID的过滤条件
     */
    Bson filterByPrincipalId(ObjectId principalId);

    /**
     * 按照主体ID来统计匹配的记录条数.
     *
     * @param principalId 主体ID
     * @return 匹配的记录条数
     */
    long countByPrincipalId(ObjectId principalId);

    /**
     * 查找主体ID下匹配的所有记录.
     *
     * @param principalId 主体ID
     * @return 匹配的所有记录
     */
    FindIterable<E> findByPrincipalId(ObjectId principalId);

    /**
     * 删除主体ID下匹配的所有记录.
     *
     * @param principalId 主体ID
     * @return 删除结果
     */
    DeleteResult deleteByPrincipalId(ObjectId principalId);

    /**
     * 与外部的主体表进行left join查询，返回实体与外部主体关联的结果列表.
     *
     * @param principalService 外部主体服务
     * @param filter           过滤条件
     * @param <P>              外部关联主体的实体类
     * @param <SPT>            外部关联主体的服务类
     * @return 实体与外部主体关联的结果列表
     */
    <P extends Entity, SPT extends Service<P>> MongoIterable<Entry<E, P>> findWithPrincipal(SPT principalService,
                                                                                            Bson filter);
}
