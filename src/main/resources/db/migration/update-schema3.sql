-- First, drop the foreign key constraint from product table
ALTER TABLE product DROP FOREIGN KEY FK1mtsbur82frn64de7balymq9s;

-- Modify category table to add AUTO_INCREMENT to id column
ALTER TABLE category MODIFY id BIGINT NOT NULL AUTO_INCREMENT;

-- Modify product table to add AUTO_INCREMENT to id column
ALTER TABLE product MODIFY id BIGINT NOT NULL AUTO_INCREMENT;

-- Recreate the foreign key constraint
ALTER TABLE product ADD CONSTRAINT FK1mtsbur82frn64de7balymq9s FOREIGN KEY (category_id) REFERENCES category(id);
