/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcom.com.its.notify.recevice.service.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Admin
 */
@Builder
@Data
@NoArgsConstructor
@ToString
public class Point {

    private static final double R = 6371e3;

    private double longitude;
    private double latitude;

    public static Point getPoint(double bearing, Point point, float distance) {
        bearing =  (bearing * Math.PI / 180);
        double longitude = (point.getLongitude() * Math.PI / 180);
        double latitude = (point.getLatitude() * Math.PI / 180);
        double desLatitude = Math.asin(Math.sin(latitude) * Math.cos(distance / R)
                + Math.cos(latitude) * Math.sin(distance / R) * Math.cos(bearing));
        double desLongitude = (longitude + Math.atan2(Math.sin(bearing) * Math.sin(distance / R) * Math.cos(latitude),
                Math.cos(distance / R) - Math.sin(latitude) * Math.sin(desLatitude)));

        Point destinationPoint = new Point();
        destinationPoint.setLongitude((desLongitude * 180.0D / 3.141592653589793D + 360.0D) % 360.0D);
        destinationPoint.setLatitude((desLatitude * 180.0D / 3.141592653589793D + 360.0D) % 360.0D);
        return destinationPoint;

    }

    public Point(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

}
