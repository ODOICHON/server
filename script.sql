SET GLOBAL log_bin_trust_function_creators = 1;

CREATE FUNCTION `strip_tags`($str text) RETURNS text
BEGIN
    DECLARE $start, $end INT DEFAULT 1;
    LOOP
SET $start = LOCATE('<', $str, $start);
        IF (!$start) THEN RETURN $str; END IF;
        SET $end = LOCATE('>', $str, $start);
        IF (!$end) THEN SET $end = $start; END IF;
        SET $str = INSERT($str, $start, $end - $start + 1, "");
END LOOP;
END;

SET GLOBAL log_bin_trust_function_creators = 0;