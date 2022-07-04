--call next value for hibernate_sequence;
insert into realEstate (`id`, `name`, `address`, `type`, `areaForExclusiveUse`, `marketPrice`, `buildYear`, `subways`, `supermarkets`)
values (1, '반포주공1단지', '서초구 반포동 757', '아파트', '140.13', '710000', '1973', NULL, NULL, now(), now());

--call next value for hibernate_sequence;
insert into realEstate (`id`, `name`, `address`, `type`, `areaForExclusiveUse`, `marketPrice`, `buildYear`, `subways`, `supermarkets`)
values (2, '반포자이', '서초구 반포동 20-43', '아파트', '84.943', '390000', '2009',
'{
{
"반포역 7호선",
"서울 서초구 잠원동 103",
"http://place.map.kakao.com/21160707",
"206"
},
{
"사평역 9호선",
"서울 서초구 반포동 128-7",
"http://place.map.kakao.com/7918693",
"442"
}
}',
NULL,
now(), now());

--call next value for hibernate_sequence;
insert into realEstateInfo (`id`, `name`, `address`, `type`, `areaForExclusiveUse`, `marketPrice`, `buildYear`, `subways`, `supermarkets`)
values (3, '래미안 퍼스티지', '서초구 반포동 18-1', '아파트', '84.85', '380000', '2009', NULL, NULL, now(), now());

--call next value for hibernate_sequence;
insert into realEstateInfo (`id`, `name`, `address`, `type`, `areaForExclusiveUse`, `marketPrice`, `buildYear`, `subways`, `supermarkets`)
values (4, '아크로리버파크', '서초구 반포동 2-12', '아파트', '129.97', '680000', '2016', NULL, NULL, now(), now());

--call next value for hibernate_sequence;
insert into realEstateInfo (`id`, `name`, `address`, `type`, `areaForExclusiveUse`, `marketPrice`, `buildYear`, `subways`, `supermarkets`)
values (5, '반포센트럴자이', '서초구 잠원동 162', '아파트', '84.98', '363000', '2020', NULL, NULL, now(), now());