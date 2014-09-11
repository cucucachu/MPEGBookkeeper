package mpeg_bookkeeper.jca_builder;

/**
 * Contains information about a single job.
 *
 * @author Cody Jones
 * @version 1.0
 */
public class Job implements Comparable<Job> {
   private static final String OTFlag = "o/t";

   String jobName;
   String jobNoStr;
   String initials;
   String classCode;
   double hours;
   String miles;
   String fdt;
   String other;
   String type;
   String prevWage;
   Rejection rejection;
   
   
   public Job(String jobName, String jobNoStr, String initials, String classCode,
      String hours, String miles, String fdt, String other, String type,
      String prevWage) {
      
      this.jobName = jobName;
      this.jobNoStr = jobNoStr;
      this.initials = initials.toUpperCase();
      this.classCode = classCode;
      this.hours = Double.parseDouble(hours);
      this.miles = miles;
      this.fdt = fdt;
      this.other = other;
      this.type = type;
      this.prevWage = prevWage;
      this.rejection = null;
      
      emptyStringsToNull();
      
      if (this.prevWage != null) {
         this.prevWage = this.prevWage.toUpperCase().trim();
      }
   }
   
   public void setRejection(Rejection reject) {
      this.rejection = reject;
   }
   
   public int compareTo(Job other) {
      String jobNoThis;
      String jobNoOther;
      int comparison;
      
      jobNoThis = this.jobNoStr;
      jobNoThis.replace('-', '.');
      
      jobNoOther = other.jobNoStr;
      jobNoOther.replace('-', '.');
      
      if (jobNoThis.contains(".")) {
         for (int digits = jobNoThis.indexOf('.'); digits < 6; digits++)
            jobNoThis = "0" + jobNoThis;
         for (int digits = jobNoThis.length() - 1 - jobNoThis.indexOf('.');
            digits < 6; digits++)
            jobNoThis = jobNoThis + "0";
      }
      
      if (jobNoOther.contains(".")) {
         for (int digits = jobNoOther.indexOf('.'); digits < 6; digits++)
            jobNoOther = "0" + jobNoOther;
            
         for (int digits = jobNoOther.length() - 1 - jobNoOther.indexOf('.');
            digits < 6; digits++)
            jobNoOther = jobNoOther + "0";
      }
      
      comparison = jobNoThis.compareTo(jobNoOther);
      
      return comparison;
   }
   
   public String toString() {
      return jobName + ": " + jobNoStr + " " + initials;
   }
   
   private void emptyStringsToNull() {
      if (jobName != null && jobName.isEmpty())
         jobName = null;
      if (initials != null && initials.isEmpty())
         initials = null;
      if (classCode != null && classCode.isEmpty())
         classCode = null;
      if (miles != null && miles.isEmpty())
         miles = null;
      if (fdt != null && fdt.isEmpty())
         fdt = null;
      if (other != null && other.isEmpty())
         other = null;
      if (type != null && type.isEmpty())
         type = null;
      if (prevWage != null && prevWage.isEmpty())
         prevWage = null;
   }
   
   public boolean isVehicle() {
      if (initials.contains("-v") || initials.contains("-V"))
         return true;
      if (miles != null)
         return true;
      return false;
   }
   
   public boolean isNormal() {
      if (miles == null && fdt == null && !isReimbursableCharge()
         && type == null)
         return true;
      return false;
   }
   
   public boolean isInsideCharge() {
      if (isVehicle() || fdt != null)
         return true;
      return false;
   }
   
   public boolean isReimbursableCharge() {
      if ((other != null && !other.toLowerCase().contains(OTFlag)) || type != null)
         return true;
      else
         return false;
   }
}
