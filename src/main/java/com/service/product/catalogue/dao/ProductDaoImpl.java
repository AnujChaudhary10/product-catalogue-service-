package com.service.product.catalogue.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.service.product.catalogue.common.ServiceConstant;
import com.service.product.catalogue.dto.CreateProductDTO;
import com.service.product.catalogue.dto.ProductDTO;
import com.service.product.catalogue.exception.ProductDaoException;

@Repository
public class ProductDaoImpl implements ProductDao {

	private final static String DELETE_PRODUCT = "DELETE FROM products WHERE product_id = ?";
	private final static String SELECT_QUERY = "SELECT * FROM products";
	private final static String INSERT_QUERY = " INSERT INTO products (product_id, product_name, product_type)  VALUES(?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Set<ProductDTO> findProduct() {
		Set<ProductDTO> result = jdbcTemplate.query(SELECT_QUERY,
				new ProductExtractor());
		return result;
	}

	@Override
	public String addProduct(List<CreateProductDTO> products)
			throws ProductDaoException {
		String statusOfBulkUpload = ServiceConstant.FAIL;
		List<Object[]> batchParamList = new ArrayList<Object[]>();
		try {
			for (ProductDTO product : products) {
				Object[] batchParam = new Object[] { product.getProductId(),
						product.getProductName(), product.getProductType() };
				batchParamList.add(batchParam);
			}

			int[] rowAffected = jdbcTemplate.batchUpdate(INSERT_QUERY,
					batchParamList);
			if (rowAffected.length > 0) {
				statusOfBulkUpload = ServiceConstant.CREATED;
			}
		} catch (Exception ex) {
			if (ex instanceof DuplicateKeyException) {
				throw new ProductDaoException(
						"Any one product id already exist", ex);
			}
		}
		return statusOfBulkUpload;

	}

	@Override
	public String deleteProduct(int productId) {
		String statusMessage = ServiceConstant.FAIL;
		try {
			Object[] params = { productId };
			int[] types = { Types.BIGINT };
			int rows = jdbcTemplate.update(DELETE_PRODUCT, params, types);
			if (rows > 0) {
				statusMessage = ServiceConstant.SUCCESS;
			}
		} catch (Exception ex) {
			return statusMessage;
		}
		return statusMessage;
	}

	private static final class ProductExtractor implements
			ResultSetExtractor<Set<ProductDTO>> {

		@Override
		public Set<ProductDTO> extractData(ResultSet rs) throws SQLException,
				DataAccessException {
			Set<ProductDTO> products = new LinkedHashSet<ProductDTO>();
			while (rs.next()) {
				ProductDTO product = new ProductDTO();
				product.setProductId(rs.getInt("product_id"));
				product.setProductName(rs.getString("product_name"));
				product.setProductType(rs.getString("product_type"));
				products.add(product);
			}
			return products;
		}
	}

}
