package com.signup.controller;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.signup.DAO.userDao;
import com.signup.bean.patient;
import com.signup.bean.Appointments;
import com.signup.bean.Doctor;
import com.signup.bean.Intimate;
import com.signup.bean.authentication;
import com.signup.DAO.AppointmentsDao;
import com.signup.DAO.DoctorRepo;
import com.signup.DAO.IntimateDao;
import com.signup.DAO.authenticationRepo;

@Controller
public class userController {
	@Autowired
	userDao dao;
	@Autowired
	DoctorRepo repo;
	@Autowired
	authenticationRepo authDao;
	@Autowired
	AppointmentsDao appDao;
	@Autowired
	IntimateDao intimateDao;

////////////////////////////////////////////_____HOME_URL____________/////////////////////////////////////////////

	@RequestMapping("/")
	public ModelAndView homepage() {
		ModelAndView mv = new ModelAndView();
		authentication auth = new authentication();
		auth.setUsrid(1);
		auth.setPasswd("admin123");
		auth.setUsrtype("Admin");
		authDao.save(auth);
		auth.setUsrid(2);
		auth.setPasswd("reporter123");
		auth.setUsrtype("Reporter");
		authDao.save(auth);
		mv.setViewName("Login.jsp");
		return mv;
	}

///////////////////////////////////////////_______HOME_PAGE_________/////////////////////////////////////////////

	@RequestMapping("/login")
	public ModelAndView login(@RequestParam("passwd") String passwd, @RequestParam("usrid") int usrid,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		authentication auth = authDao.findById(usrid).get();
		if (auth.getPasswd().equals(passwd) && auth.getUsrtype().equals("Admin")) {
			
			session.setAttribute("admin", auth.getUsrtype());
			
			mv.setViewName("Admin.jsp");
			mv.addObject("message", "*Login successfully.");
		} else if (auth.getPasswd().equals(passwd) && auth.getUsrtype().equals("Reporter")) {
			
			session.setAttribute("reporter", auth.getUsrtype());
			
			mv.setViewName("reporter.jsp");
			mv.addObject("message", "*Login successfully.");
		} else {
			try {
				patient p = dao.findById(usrid).get();
				session.setAttribute("usrid", p.getUsrid());
				session.setAttribute("usrname", p.getFname());
				
				if (p.getPasswd().equals(passwd)) {
					mv.setViewName("welcome.jsp");
					mv.addObject("message", "*Login successfully.");
//					mv.addObject("greeting", p.getFname());
				} else {
					mv.setViewName("Login.jsp");
					mv.addObject("message", "*Invalid Password.");
				}
			} catch (Exception e) {
				mv.setViewName("Login.jsp");
				mv.addObject("message", "*Invalid User.");
			}
		}
		return mv;
	}

////////////////////////////////////////_______SIGNUP_________/////////////////////////////////////////////////////

	@RequestMapping("/addPatient")
	public ModelAndView addPatient(patient usr) {
		ModelAndView mv = new ModelAndView();
		dao.save(usr);
		mv.setViewName("Login.jsp");
		mv.addObject("message", "*Patient inserted successfully.");
		return mv;
	}

///////////////////////////////////_______UPDATE_PATIENT_________/////////////////////////////////////////////

	@RequestMapping("/updatePatientform")
	public ModelAndView updateForm(@RequestParam("usrid") int usrid, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		patient p = dao.findById(usrid).get();
		if (session.getAttribute("usrid") != null && (int) session.getAttribute("usrid") == p.getUsrid()) {
			mv.setViewName("Update.jsp");
			mv.addObject("patient", p);
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}

	@RequestMapping("/updatePatient")
	public ModelAndView updatePatient(patient usr, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		patient p = dao.findById(usr.getUsrid()).get();
		if (session.getAttribute("usrid") != null && (int) session.getAttribute("usrid") == p.getUsrid()) {
			if (usr.getFname() != null) {
				p.setFname(usr.getFname());
			}
			if (usr.getLname() != null) {
				p.setLname(usr.getLname());
			}
			if (usr.getAge() != 0) {
				p.setAge(usr.getAge());
			}
			if (usr.getGender() != null) {
				p.setGender(usr.getGender());
			}
			if (usr.getAddress() != null) {
				p.setAddress(usr.getAddress());
			}
			if (usr.getPhno() != null) {
				p.setPhno(usr.getPhno());
			}
			dao.save(p);
			mv.setViewName("display.jsp");
			mv.addObject("message", "*Patient updated successfully.");
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}

///////////////////////////////////_______DISPLAY_PATIENT_________/////////////////////////////////////////////

	@RequestMapping(value = "/show")
	public ModelAndView showDetails(patient usr, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("usrid") != null && (int) session.getAttribute("usrid") == usr.getUsrid()) {
			mv.addObject("details", dao.getById(usr.getUsrid()));
			mv.setViewName("display.jsp");
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value = "/bookAppointment")
	public ModelAndView bookAppointment(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("usrid") != null) {
			ArrayList<Doctor> docArray = (ArrayList<Doctor>) repo.findAll();
			mv.setViewName("bookApp1.jsp");
			mv.addObject("allDoc", docArray);
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}
	
	@RequestMapping(value = "/booking")
	public ModelAndView booking(Appointments app, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		try {
		if (session.getAttribute("usrid") != null) {
			appDao.save(app);
			mv.setViewName("welcome.jsp");
			mv.addObject("message", "*Appointment Booked Successfully.");
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		}
		catch (Exception e) {
			mv.setViewName("bookApp2.jsp");
			mv.addObject("message", "*Slot already booked.");	
		}
		return mv;
	}
	
	@RequestMapping("/bookApp")
	public ModelAndView bookApp(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("usrid") != null) {
			ArrayList<Appointments> appArray = (ArrayList<Appointments>) appDao.findAll();
			mv.setViewName("bookApp2.jsp");
			mv.addObject("allApp", appArray);
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////_______ADMIN_PART_________/////////////////////////////////////////////

	@RequestMapping("/Admin")
	public String admin() {
		return "Admin.jsp";
	}

	@RequestMapping("/AddDoctorDetails")
	public ModelAndView AddDoctorDetails( HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("admin") != null) {
			mv.setViewName("AddDoctorDetails.jsp");
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}

	@RequestMapping("/DeleteDoctorDetails")
	public ModelAndView DeleteDoctorDetails(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("admin") != null) {
			mv.setViewName("DeleteDoctorDetails.jsp");
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}
	
	@RequestMapping("/forgotAdminPassword")
	public ModelAndView forgotAdminPassword(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("admin") != null) {
			mv.setViewName("forgotAdminPassword.jsp");
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}

	@RequestMapping("/ModifyDoctorDetails")
	public ModelAndView ModifyDoctorDetails(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("admin") != null) {
			mv.setViewName("ModifyDoctorDetails.jsp");
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}

	@RequestMapping("/UpdateDoctorDetailsform")
	public ModelAndView UpdateDoctorDetails(@RequestParam("DocID") int DocID,HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("admin") != null) {
		Doctor dc = repo.findById(DocID).get();
		mv.setViewName("UpdateDoctorDetails.jsp");
		mv.addObject("doc", dc);
		}
		else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;

	}

	@RequestMapping("/UpdateDoctor")
	public ModelAndView UpdateDoctorDetsails(Doctor doc,HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("admin") != null) {
		Doctor d = repo.findById(doc.getDocID()).get();
		if (doc.getDocName() != null) {

			d.setDocName(doc.getDocName());
		}
		if (doc.getDocDOB() != null) {
			d.setDocDOB(doc.getDocDOB());
		}
		if (doc.getDocDOJ() != null) {
			d.setDocDOJ(doc.getDocDOJ());
		}
		if (doc.getDocGender() != null) {
			d.setDocGender(doc.getDocGender());
		}
		if (doc.getDocQualification() != null) {
			d.setDocQualification(doc.getDocQualification());
		}
		if (doc.getDocSpecialization() != null) {
			d.setDocSpecialization(doc.getDocSpecialization());
		}
		if (doc.getDocYearsofExperience() != 0) {
			d.setDocYearsofExperience(doc.getDocYearsofExperience());
		}
		if (doc.getDocAddress() != null) {
			d.setDocAddress(doc.getDocAddress());
		}
		if (doc.getDocContactNumber() != 0) {
			d.setDocContactNumber(doc.getDocContactNumber());
		}
		if (doc.getAvailabilityStatus() != null) {
			d.setAvailabilityStatus(doc.getAvailabilityStatus());
		}
		repo.save(d);
		mv.setViewName("Admin.jsp");
		mv.addObject("message", "*Doctor details updated successfully.");
		}
		else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}

	@RequestMapping("/ViewDoctorDetails")
	public ModelAndView ViewDoctorDetails(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("admin") != null) {
			mv.setViewName("ViewDoctorDetails.jsp");
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}

	@RequestMapping("/addDoc")
	public ModelAndView addDoc(Doctor doctor,HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("admin") != null) {
			repo.save(doctor);
			mv.setViewName("SuccessfullyAddedDoctorDetails.jsp");
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}

	@RequestMapping("/DeleteDoctor")
	public ModelAndView DeleteDoctor(@RequestParam int DocID,HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("admin") != null) {
			repo.deleteById(DocID);
			mv.setViewName("SuccessfullyDeletedDoctorDetails.jsp");
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}

	@RequestMapping("/ModifyDocDetails")
	public ModelAndView ModifyDocDetails(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("admin") != null) {
			mv.setViewName("UpdateDoctorDetails.jsp");
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}

	@PostMapping("/getDetails")
	public ModelAndView getDetails(@RequestParam int DocID,HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("admin") != null) {
			Doctor doctor = repo.findById(DocID).orElse(null);
			mv.setViewName("SuccessfullyViewingDoctorDetails.jsp");
			mv.addObject(doctor);
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}
	
	@RequestMapping("/leaveDetails")
	public ModelAndView leaveDetails(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("admin") != null) {
			ArrayList<Intimate> intimateArray = (ArrayList<Intimate>) intimateDao.findAll();
			mv.setViewName("leaveDetails.jsp");
			mv.addObject("details", intimateArray);
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}
	
	@RequestMapping(value = "/ViewAllDoctor")
	public ModelAndView ViewAllDoctor(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("admin") != null) {
			ArrayList<Doctor> docArray = (ArrayList<Doctor>) repo.findAll();
			mv.setViewName("ViewAllDoctor.jsp");
			mv.addObject("allDoc", docArray);
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////_______REPORTER_PART_________///////////////////////////////////////
	
	@RequestMapping("/forgotReporterPassword")
	public ModelAndView forgotReporterPassword(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("reporter") != null) {
			mv.setViewName("forgotReporterPassword.jsp");
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}

	@RequestMapping("/leave")
	public ModelAndView leave(@RequestParam("docId") int docId,@RequestParam("status") String status, HttpSession session,Intimate intimate) {
		ModelAndView mv = new ModelAndView();
		Appointments p = appDao.getById(docId);
		if (session.getAttribute("reporter") != null) {
			intimate.setDocID(p.getDocID());
			intimate.setDate(p.getDate());
			intimate.setStatus(status);
			intimateDao.save(intimate);
			mv.setViewName("reporter.jsp");
			mv.addObject("message", "*Status Updated.");
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////	
///////////////////////////////////_______CHANGE_PASSWORD_________//////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////	

///////////////////////////////////_______ADMIN_PASSWORD_________///////////////////////////////////////

	@RequestMapping("/changeAdminPasswd")
	public ModelAndView changeAdminPasswd(@RequestParam("passwd") String passwd, @RequestParam("usrid") int usrid,HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("admin") != null) {
		authentication admin = authDao.findById(usrid).get();
		admin.setPasswd(passwd);
		authDao.save(admin);
		mv.setViewName("Admin.jsp");
		mv.addObject("message", "*Password Updated successfully.");
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}

///////////////////////////////////_______REPORTER_PASSWORD_________/////////////////////////////////////////////

	@RequestMapping("/changeReporterPasswd")
	public ModelAndView changeReporterPasswd(@RequestParam("passwd") String passwd, @RequestParam("usrid") int usrid,HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("reporter") != null) {
		authentication reporter = authDao.findById(usrid).get();
		reporter.setPasswd(passwd);
		authDao.save(reporter);
		mv.setViewName("reporter.jsp");
		mv.addObject("message", "*Password Updated successfully.");
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}
///////////////////////////////////_______PATIENT_PASSWORD_________/////////////////////////////////////////////

	@RequestMapping("/changeUserPasswd")
	public ModelAndView changeUserPasswd(@RequestParam("passwd") String passwd, @RequestParam("usrid") int usrid,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		patient p = dao.findById(usrid).get();
		if (session.getAttribute("usrid") != null && (int) session.getAttribute("usrid") == p.getUsrid()) {
			p.setPasswd(passwd);
			dao.save(p);
			mv.setViewName("welcome.jsp");
			mv.addObject("message", "*Password Updated successfully.");
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}

///////////////////////////////////_______Exit_Handler_________/////////////////////////////////////////////

	@RequestMapping("/displayPageHandler")
	public ModelAndView sessionPageHandler(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("usrid") != null) {
			mv.setViewName("display.jsp");
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}
	
	@RequestMapping("/passwordPageHandler")
	public ModelAndView passwordPageHandler(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("usrid") != null) {
			mv.setViewName("forgotUserPassword.jsp");
		} else {
			mv.setViewName("Login.jsp");
			mv.addObject("message", "*Session Expired.");
		}
		return mv;
	}

///////////////////////////////////_______Exit_Handler_________/////////////////////////////////////////////

	@RequestMapping("/adminLogout")
	public ModelAndView adminLogout(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		session.removeAttribute("Admin");
		mv.setViewName("Login.jsp");
		mv.addObject("message", "Logged out successfully.");
		return mv;
	}
	
	@RequestMapping("/reporterLogout")
	public ModelAndView reporterlogout(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		session.removeAttribute("reporter");
		mv.setViewName("Login.jsp");
		mv.addObject("message", "Logged out successfully.");
		return mv;
	}
	
	@RequestMapping("/patientLogout")
	public ModelAndView patientlogout(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		session.removeAttribute("usrid");
		mv.setViewName("Login.jsp");
		mv.addObject("message", "Logged out successfully.");
		return mv;
	}

}
