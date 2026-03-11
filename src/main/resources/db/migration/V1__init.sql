create table app_user (
    id bigserial primary key,
    username varchar(100) not null unique,
    password_hash varchar(200) not null,
    role varchar(30) not null,
    enabled boolean not null default true,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create table job_application (
    id bigserial primary key,
    applicant_user_id bigint not null references app_user(id),
    status varchar(30) not null,
    decision_reason varchar(500),
    reviewed_by_user_id bigint references app_user(id),
    reviewed_at timestamptz,

    nid_number varchar(50) not null,
    nid_first_name varchar(100) not null,
    nid_last_name varchar(100) not null,
    nid_date_of_birth date not null,

    nesa_candidate_id varchar(50) not null,
    nesa_grade varchar(50) not null,
    nesa_option_attended varchar(100) not null,

    cv_original_filename varchar(255) not null,
    cv_content_type varchar(100) not null,
    cv_size_bytes bigint not null,
    cv_storage_path varchar(500) not null,

    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create index idx_job_application_created_at on job_application(created_at desc);
create index idx_job_application_applicant on job_application(applicant_user_id);
create index idx_job_application_name on job_application(nid_last_name, nid_first_name);
