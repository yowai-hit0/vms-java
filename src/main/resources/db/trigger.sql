-- Create a function to handle the trigger
CREATE OR REPLACE FUNCTION create_salary_notification()
RETURNS TRIGGER AS $$
DECLARE
    employee_first_name TEXT;
    employee_email TEXT;
BEGIN
    -- Get employee details
    SELECT u.first_name, u.email INTO employee_first_name, employee_email
    FROM users u
    WHERE u.id = NEW.employee_id;

    -- Create message for notification
    INSERT INTO messages (
        id,
        employee_id,
        message,
        month,
        year,
        sent,
        sent_at
    ) VALUES (
        gen_random_uuid(),
        NEW.employee_id,
        FORMAT('Dear %s, your salary for %s/%s from RCA amounting to %s has been credited to your account %s successfully.',
            employee_first_name,
            NEW.month,
            NEW.year,
            NEW.net_salary,
            NEW.employee_id
        ),
        NEW.month,
        NEW.year,
        FALSE,
        NULL
    );

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create the trigger
DROP TRIGGER IF EXISTS payslip_status_update_trigger ON payslips;
CREATE TRIGGER payslip_status_update_trigger
AFTER UPDATE OF status ON payslips
FOR EACH ROW
WHEN (OLD.status = 'PENDING' AND NEW.status = 'PAID')
EXECUTE FUNCTION create_salary_notification();