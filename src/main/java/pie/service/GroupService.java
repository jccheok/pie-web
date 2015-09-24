package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import pie.Group;
import pie.GroupType;
import pie.util.DatabaseConnector;

public class GroupService {

	public Group getGroup(int groupID) {
		Group group = null;

		try {

			PreparedStatement pst = null;
			ResultSet resultSet = null;
			Connection conn = DatabaseConnector.getConnection();

			String sql = "SELECT * FROM `Group` WHERE groupID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, groupID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				group = new Group();
				group.setGroupID(groupID);
				group.setSchool(SchoolService.getSchool(resultSet.getInt("schoolID")));
				group.setGroupDescription(resultSet.getString("groupDescription"));
				group.setGroupName(resultSet.getString("groupName"));
				group.setGroupMaxDailyHomeworkMinutes(resultSet.getInt("groupMaxDailyHomeworkMinutes"));
				group.setGroupType(GroupType.getGroupType(resultSet.getInt("groupTypeID")));
				group.setGroupCode(resultSet.getString("groupCode"));
				group.setGroupIsOpen(resultSet.getInt("groupIsOpen")==1);
				group.setGroupLastUpdate(new Date(resultSet.getTimestamp(
						"groupLastUpdate").getTime()));
				group.setGroupDateCreated(new Date(resultSet.getTimestamp(
						"groupDateCreated").getTime()));
			}

		} catch (Exception e) {
			System.out.println(e);
		}

		return group;
	}
	
	public int getTotalHomeworkMinutesToday(int groupID){
		
		int totalMinutes = 0;
		
		//Write codes to retrieve total minutes of homework for the day
		
		return totalMinutes;
		
	}
	
	
	
	
	
	
}
