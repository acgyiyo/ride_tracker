package com.pluralsight.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.pluralsight.model.Ride;
import com.pluralsight.repository.util.RideRowMapper;

@Repository("rideRepository")
public class RideRepositoryImpl implements RideRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Ride createRideSimple(Ride ride) {
		// esta es una manera simple y rápida de hacer una consulta
		jdbcTemplate.update("insert into ride (name,duration) values (?,?)", ride.getName(), ride.getDuration());

		return null;
	}

	@Override
	public Ride createRideReturningKey(Ride ride) {
		// esta es otra manera, la ventaja es que devuelve el id del registro insertado
		SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);

		List<String> columns = new ArrayList<>();
		columns.add("name");
		columns.add("duration");

		insert.setTableName("ride");
		insert.setColumnNames(columns);

		Map<String, Object> data = new HashMap<>();
		data.put("name", ride.getName());
		data.put("duration", ride.getDuration());

		insert.setGeneratedKeyName("id");

		Number key = insert.executeAndReturnKey(data);

		// imprimimos el id del resultado insertado
		ride = new Ride();
		ride.setId(key.intValue());
		return ride;
	}

	@Override
	public Ride createRideKeyHolder(Ride ride) {
		// devolvemos key del registro unsertado usando un prepareStatement y un
		// keyholder

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement("insert iinto ride (name,duration) values (?,?)",
					new String[] { "id" });
			ps.setString(1, ride.getName());
			ps.setInt(2, ride.getDuration());
			return ps;
		}, keyHolder);

		Number id = keyHolder.getKey();

		return getRide(id.intValue());
	}

	@Override
	public Ride getRide(Integer id) {
		Ride ride = jdbcTemplate.queryForObject("select * from ride where id = ?", new RideRowMapper(), id);
		return ride;
	}

	@Override
	public List<Ride> getRides() {
		List<Ride> rides = jdbcTemplate.query("select * from ride", (RowMapper<Ride>) (rs, rowNum) -> {
			Ride ride = new Ride();
			ride.setId(rs.getInt("id"));
			ride.setName(rs.getString("name"));
			ride.setDuration(rs.getInt("duration"));
			return ride;
		});

		// otra forma es crear un rowMapper y hacer el llamado de la siguiente forma
		List<Ride> rides2 = jdbcTemplate.query("select * from ride", new RideRowMapper());

		return rides2;
	}

	@Override
	public Ride updateRide(Ride ride) {
		jdbcTemplate.update("update ride set name = ?, duration = ? where id = ?", ride.getName(), ride.getDuration(),
				ride.getId());
		return ride;
	}

	@Override
	public void updateBatchRide(List<Object[]> updates) {
		jdbcTemplate.batchUpdate("update ride set duration = ?,ride_date = ? where id = ?", updates);
	}

	@Override
	public void deleteRide(Integer id) {
		jdbcTemplate.update("delete from ride where id = ?", id);
//		deleteRideWithNamedParam(id);
	}
	
	@Override
	public void deleteRideWithNamedParam(Integer id) {
		NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		Map<String,Object> paramMap = new HashMap<>();
		
		paramMap.put("id", id);
		System.out.println("estoy a punto de borrar");
		int borrados=namedTemplate.update("delete from ride where id = :id", paramMap);
		System.out.println("Borre algo?->> "+ borrados);
	}

}
