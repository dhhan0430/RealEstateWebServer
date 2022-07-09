package com.fastcampus.jpa.bookmanager2.domain.converter;

import com.fastcampus.jpa.bookmanager2.repository.dto.BookStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

// 컨버터를 자주 사용하게 될 때 autoApply 옵션을 쓰는 것이 좋다.
// 그러면 실제 Book 클래스의 BookStatus field에 @Convert 를 작성 안 해줘도 된다.
//@Converter(autoApply = true)
// <X, Y> X: entity attribute, Y: database column
public class BookStatusConverter implements AttributeConverter<BookStatus, Integer> {

    // convertToDatabaseColumn() 구현 안하면 발생하는 일:
    // 일단 data.sql 로 인해 data들은 DB에 잘 저장됐고, findAll() 하면 해당 데이터들을
    // convertToEntityAttribute() 에 의해 잘 가져올 수 있다.
    // 하지만, BookService 의 getAll() 을 Transactional 로 걸어놓으면,
    // 해당 세션이 끝나기 전에 영속성 컨텍스트는 해당 entity의 값 중에서
    // 변경된 내용이 있는지 없는지 체크를 하고, 만약에 변경된 내용이 있다고 한다면,
    // 그 데이터를 DB에 다시 영속화를 하게 된다.
    // 가져올 땐 DB에 있는 값을 convertToEntityAttribute()을 통해 잘 가져왔는데,
    // 영속성 컨텍스트가 해당 entity의 값 중에서 변경된 내용이 있는지 체크를 할 때
    // convertToDatabaseColumn()을 통해 확인하는 것으로 보인다.
    // 하지만 해당 함수가 구현되어 있지 않으면, return null; (default) 로 되어 있기 때문에
    // 현재 값이 null인 줄 알고, null 값을 update 쿼리로 업데이트 시켜준다.
    @Override
    public Integer convertToDatabaseColumn(BookStatus attribute) {

        return attribute.getCode();
    }

    @Override
    public BookStatus convertToEntityAttribute(Integer dbData) {
        return dbData != null ? new BookStatus(dbData) : null;
    }
}
