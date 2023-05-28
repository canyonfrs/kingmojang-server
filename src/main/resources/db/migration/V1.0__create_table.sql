create table creator_information
(
    creator_information_id bigint not null auto_increment comment 'PK',
    banner_image           varchar(255) comment '배너 이미지',
    broadcast_link         varchar(255) comment '방송 주소',
    donation_link          varchar(255) comment '도네이션 주소',
    introduce              text comment '자기소개',
    youtube                varchar(255) comment '유튜브 주소',
    primary key (creator_information_id)
) engine = InnoDB;

create table highlight
(
    highlight_id bigint  not null auto_increment comment 'PK',
    content      mediumtext comment '하이라이트 내용',
    end_x        integer not null comment '시작 좌표 x',
    end_y        integer not null comment '시작 좌표 y',
    start_x      integer not null comment '끝 좌표 x',
    start_y      integer not null comment '끝 좌표 y',
    version      integer not null comment '메모 버젼',
    primary key (highlight_id)
) engine = InnoDB;

create table member
(
    member_id              bigint                    not null auto_increment comment 'PK',
    created_at             datetime(6)               not null comment '회원가입 일자',
    deleted                bit         default false not null comment '탈퇴여부',
    deleted_at             datetime(6) default null comment '탈퇴 날짜',
    email                  varchar(255)              not null comment '이메일',
    nickname               varchar(255)              not null comment '닉네임',
    `password`             varchar(255)              not null comment '비밀번호',
    profile_image          varchar(255) comment '프로필 이미지',
    provider               varchar(255) comment '회원가입 경로',
    token_expired_date     datetime(6) comment '토큰 만료 일자',
    refresh_token          varchar(255) comment '리프레시 토큰',
    type                   varchar(255) comment '회원 권한',
    follower_count         integer     default 0     not null comment '팔로워 수',
    creator_information_id bigint comment '크리에이터 정보 FK',
    primary key (member_id),
    constraint `email_uk` unique (email),
    constraint fk_member_creator_information_id foreign key (creator_information_id) references creator_information (creator_information_id)
) engine = InnoDB;

create table authentication_code
(
    authentication_code_id bigint            not null auto_increment comment 'PK',
    `code`                 integer           not null comment '인증코드',
    created_at             datetime(6)       not null comment '생성일자',
    email                  varchar(255) comment '이메일',
    is_expired             bit default false not null comment '만료여부',
    member_id              bigint comment '추천인',
    primary key (authentication_code_id),
    constraint fk_authentication_code_member_id foreign key (member_id) references member (member_id)
) engine = InnoDB;

create table emoji
(
    emoji_id   bigint      not null auto_increment comment 'PK',
    created_at datetime(6) not null comment '생성일자',
    price      integer     not null default 0 comment '가격',
    member_id  bigint comment '이모지 제작자',
    primary key (emoji_id),
    constraint fk_emoji_member_id foreign key (member_id) references member (member_id)
) engine = InnoDB;

create table follow
(
    follow_id   bigint      not null auto_increment comment 'PK',
    created_at  datetime(6) not null comment '팔로우 일자',
    creator_id  bigint comment '크리에이터 FK',
    follower_id bigint comment '팔로워 FK',
    primary key (follow_id),
    constraint fk_follow_creator_id foreign key (creator_id) references member (member_id),
    constraint fk_follow_follower_id foreign key (follower_id) references member (member_id)
) engine = InnoDB;

create table memo
(
    memo_id       bigint            not null auto_increment comment 'PK',
    content       longtext comment '내용',
    title         varchar(255) comment '제목',
    created_at    datetime(6)       not null comment '생성일자',
    updated_at    datetime(6)       not null comment '수정일자',
    font_name     varchar(255) comment '폰트 이름',
    font_size     integer comment '폰트 사이즈',
    font_style    varchar(255) comment '폰트 스타일',
    comment_count integer default 0 not null comment '댓글 수',
    like_count    integer default 0 not null comment '좋아요 수',
    version       integer default 0 not null comment '버젼',
    member_id     bigint comment '작성자 FK',
    primary key (memo_id),
    constraint fk_memo_member_id foreign key (member_id) references member (member_id)
) engine = InnoDB;

create table `comment`
(
    comment_id   bigint            not null auto_increment comment 'PK',
    content      longtext comment '내용',
    created_at   datetime(6)       not null comment '생성일자',
    updated_at   datetime(6)       not null comment '수정일자',
    like_count   integer default 0 not null comment '좋아요 수',
    reply_count  integer default 0 not null comment '답글 수',
    highlight_id bigint comment '하이라이트 FK',
    memo_id      bigint comment '메모 FK',
    member_id    bigint comment '작성자 FK',
    primary key (comment_id),
    constraint fk_comment_highlight_id foreign key (highlight_id) references highlight (highlight_id),
    constraint fk_comment_memo_id foreign key (memo_id) references memo (memo_id),
    constraint fk_comment_member_id foreign key (member_id) references member (member_id)
) engine = InnoDB;

create table reply
(
    reply_id   bigint            not null auto_increment comment 'PK',
    content    longtext comment '내용',
    created_at datetime(6)       not null comment '생성일자',
    updated_at datetime(6)       not null comment '수정일자',
    like_count integer default 0 not null comment '좋아요 수',
    comment_id bigint comment '댓글 FK',
    memo_id    bigint comment '메모 FK',
    member_id  bigint comment '작성자 FK',
    primary key (reply_id),
    constraint fk_reply_comment_id foreign key (comment_id) references `comment` (comment_id),
    constraint fk_reply_memo_id foreign key (memo_id) references memo (memo_id),
    constraint fk_reply_member_id foreign key (member_id) references member (member_id)
) engine = InnoDB;

create table memo_like
(
    memo_like_id bigint      not null auto_increment comment 'PK',
    created_at   datetime(6) not null comment '생성일자',
    member_id    bigint comment '회원 FK',
    memo_id      bigint comment '메모 FK',
    primary key (memo_like_id),
    constraint fk_memo_like_member_id foreign key (member_id) references member (member_id),
    constraint fk_memo_like_memo_id foreign key (memo_id) references memo (memo_id)
) engine = InnoDB;

create table comment_like
(
    comment_like_id bigint      not null auto_increment comment 'PK',
    created_at      datetime(6) not null comment '생성일자',
    member_id       bigint comment '회원 FK',
    comment_id      bigint comment '댓글 FK',
    primary key (comment_like_id),
    constraint fk_comment_like_comment_id foreign key (comment_id) references `comment` (comment_id),
    constraint fk_comment_like_member_id foreign key (member_id) references member (member_id)
) engine = InnoDB;

create table reply_like
(
    reply_like_id bigint      not null auto_increment comment 'PK',
    created_at    datetime(6) not null comment '생성일자',
    member_id     bigint comment '회원 FK',
    reply_id      bigint comment '답글 FK',
    primary key (reply_like_id),
    constraint fk_reply_like_member_id foreign key (member_id) references member (member_id),
    constraint fk_reply_like_reply_id foreign key (reply_id) references reply (reply_id)
) engine = InnoDB;

create table sticker
(
    sticker_id bigint not null auto_increment comment 'PK',
    address    varchar(255) comment '주소',
    emoji_id   bigint comment '이모지 FK',
    primary key (sticker_id),
    constraint fk_sticker_emoji_id foreign key (emoji_id) references emoji (emoji_id)
) engine = InnoDB;


create table retained_emoji
(
    retained_emoji_id bigint      not null auto_increment comment 'PK',
    created_at        datetime(6) not null comment '생성일자',
    emoji_status      varchar(255) comment '이모지 상태',
    expired_at        datetime(6) not null comment '만료일자',
    how_to_get        varchar(255) comment '획득 경로',
    emoji_id          bigint comment '이모지 FK',
    member_id         bigint comment '회원 FK',
    primary key (retained_emoji_id),
    constraint fk_retained_emoji_emoji_id foreign key (emoji_id) references emoji (emoji_id),
    constraint fk_retained_emoji_member_id foreign key (member_id) references member (member_id)
) engine = InnoDB;