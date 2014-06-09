// This query was originally taken from bgservice. It may contain values
// that are not currently used by PHP. Needs audit of usage in PHP and cleanup

SELECT
// --------------- base ----------------------    
    eu.user_login_id AS id,
    eu.rank_id,
    CASE WHEN (eu.distributor_status in ('A','B')) THEN 1 ELSE 0 END AS is_active_boolean,
    
// --------------- P1 ------------------------    
    eu.p1_first_name     AS p1#first_name,
    eu.p1_middle_initial AS p1#middle_initial,
    eu.p1_last_name      AS p1#last_name,
    eu.p1_birthday       AS p1#birthday,

// --------------- P2 ------------------------    
    eu.p2_first_name     AS p2#first_name,
    eu.p2_middle_initial AS p2#middle_initial,
    eu.p2_last_name      AS p2#last_name,
    eu.p2_birthday       AS p2#birthday,

// --------------- mailing address -----------    
    eu.mailing_address1 AS mailing#address1,
    eu.mailing_address2 AS mailing#address2,
    eu.city             AS mailing#city,
    eu.state_code       AS mailing#state_code,
    eu.postal_code      AS mailing#postal_code,
    eu.county           AS mailing#county,
    eu.country_code     AS mailing#country_code,
    
// --------------- shipping address -----------    
    eu.ship_address1     AS ship#address1,
    eu.ship_address2     AS ship#address2,
    eu.ship_city         AS ship#city,
    eu.ship_state_code   AS ship#state_code,
    eu.ship_postal_code  AS ship#postal_code,
    eu.shipping_county   AS ship#county,
    eu.ship_country_code AS ship#country_code,
    eu.ship_phone        AS ship#phone,

// --------------- contact ---------------------    
    eu.day_phone        AS phone,
    // eu.phone_2			AS phone2,
    // eu.fax              AS fax,
    eu.email            AS email,

// --------------- other ---------------------    
    // INTEGER(eu.service_charge) AS service_charge,
    eu.full_name         AS DBA,
    eu.price_type,
    // eu.statement_exempt  AS statement_exempt_boolean,
    // eu.tinfnty_plan,
    // COALESCE(eu.PREFERRED_DISPLAY_NAME, (COALESCE(eu.p1_first_name, '') || ' ' || COALESCE(
    // eu.p1_last_name, '') ) ) AS display_name ,
    // eu.pc_exempt_flag        AS pc_exempt_flag_boolean,
    // eu.pg_bonus_percent,
    // eu.sales_plan_allowance_date,
    ut.long_desc   AS long_title,
    eu.ship_verify AS ship_verify,
    eu.bill_verify AS bill_verify,
    // eu.sales_plan,
    ut.short_desc AS short_title,
    ut.is_bl      AS is_bl_boolean,
    // eu.renewal_date,
    eu.cid        AS cid,
    eu.leadsource AS lead_source,
    // eu.FASTTRACK_RESET_STATUS AS FASTTRACK_RESET_STATUS_boolean,
    eu.ETL_UPD_TSTAMP         AS updates#ext_time,
    ext_updt.UPDATED_ON       AS updates#updates_time,
    ext_updt.JSON             AS updates#json
FROM
    ext_user eu
LEFT JOIN ext_user_title ut
	ON ut.rank_code = eu.title_code
LEFT JOIN EXT_USER_UPDATES ext_updt
	ON eu.USER_LOGIN_ID = ext_updt.id 
WHERE
    eu.user_login_id = ${id}